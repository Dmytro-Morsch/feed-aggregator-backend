package feedaggregator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import feedaggregator.dto.ItemDto;
import feedaggregator.dto.SubscriptionDto;
import feedaggregator.module.Subscription;
import feedaggregator.module.UserItem;
import feedaggregator.repository.FeedRepository;
import feedaggregator.repository.ItemRepository;
import feedaggregator.repository.SubscriptionRepository;
import feedaggregator.service.FeedDownloader;
import feedaggregator.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FeedController {
    @Autowired
    private FeedDownloader feedDownloader;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping(value = "/api/feeds/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody String feedLink,
                                       @RequestAttribute Long userId) {
        Subscription subscription = subscriptionService.subscribe(feedLink, userId);
        feedDownloader.asyncDownloadFeed(subscription.getFeed());
        return ResponseEntity.ok(SubscriptionDto.fromEntity(subscription, 0L));
    }

    @DeleteMapping("/api/feeds/{feedId}/unsubscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable Long feedId,
                                         @RequestAttribute Long userId) {
        subscriptionRepository.unsubscribe(feedId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/feeds")
    public ResponseEntity<?> getFeeds(@RequestAttribute Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.getSubscriptions(userId);
        List<SubscriptionDto> subscriptionDtos = subscriptions.stream()
                .map(subscription -> SubscriptionDto.fromEntity(subscription, itemRepository.getUnreadItemsCount(subscription.getFeed().getId(), userId)))
                .toList();
        return ResponseEntity.ok(subscriptionDtos);
    }

    @GetMapping("/api/feeds/{id}")
    public ResponseEntity<?> getFeed(@PathVariable Long id,
                                     @RequestAttribute Long userId) {
        Subscription subscription = subscriptionRepository.getSubscription(id, userId);
        if (subscription == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(SubscriptionDto.fromEntity(subscription, itemRepository.getUnreadItemsCount(id, userId)));
    }

    @GetMapping("/api/feeds/{id}/icon")
    public ResponseEntity<?> getFeedIcon(@PathVariable Long id) {
        byte[] icon = feedRepository.getIcon(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf("image/x-icon")).body(icon);
    }

    @PostMapping("/api/feeds/{id}/update")
    public ResponseEntity<?> updateFeed(@PathVariable Long id,
                                        @RequestParam boolean descOrder,
                                        @RequestAttribute Long userId) throws IOException, ParserConfigurationException, InterruptedException, SAXException {
        feedDownloader.downloadFeed(id);
        List<UserItem> userItems = itemRepository.getUserItems(userId, id, descOrder, false, false);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (UserItem userItem : userItems) {
            itemDtos.add(ItemDto.fromEntity(userItem.item(), userItem.item().getFeed().getId(), userItem.read(), userItem.starred()));
        }
        return ResponseEntity.ok(itemDtos);
    }

    @PatchMapping(value = "/api/feeds/{feedId}/rename", consumes = "application/json")
    public ResponseEntity<?> renameFeed(@PathVariable Long feedId,
                                        @RequestBody JsonNode body,
                                        @RequestAttribute Long userId) {
        subscriptionService.renameSubscription(feedId, body.textValue(), userId);
        return ResponseEntity.noContent().build();
    }
}
