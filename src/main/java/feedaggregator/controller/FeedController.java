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

    @PostMapping("/api/feeds/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody String feedLink) {
        Subscription subscription = subscriptionService.subscribe(feedLink);
        feedDownloader.asyncDownloadFeed(subscription.getFeed());
        return ResponseEntity.ok(SubscriptionDto.fromEntity(subscription));
    }

    @DeleteMapping("/api/feeds/{feedId}/unsubscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable Long feedId) {
        subscriptionRepository.unsubscribe(feedId, 1L);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/feeds")
    public ResponseEntity<?> getFeeds() {
        List<Subscription> subscriptions = subscriptionRepository.getSubscriptions(1L);
        List<SubscriptionDto> subscriptionDtos = subscriptions.stream().map(SubscriptionDto::fromEntity).toList();
        return ResponseEntity.ok(subscriptionDtos);
    }

    @GetMapping("/api/feeds/{id}/icon")
    public ResponseEntity<?> getFeedIcon(@PathVariable Long id) {
        byte[] icon = feedRepository.getIcon(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf("image/x-icon")).body(icon);
    }

    @PostMapping("/api/feeds/{id}/update")
    public ResponseEntity<?> updateFeed(@PathVariable Long id,
                                        @RequestParam boolean descOrder) throws IOException, ParserConfigurationException, InterruptedException, SAXException {
        feedDownloader.downloadFeed(id);
        List<UserItem> userItems = itemRepository.getUserItems(1L, id, descOrder, false);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (UserItem userItem : userItems) {
            itemDtos.add(ItemDto.fromEntity(userItem.item(), userItem.read()));
        }
        return ResponseEntity.ok(itemDtos);
    }

    @PatchMapping(value = "/api/feeds/{feedId}/rename", consumes = "application/json")
    public ResponseEntity<?> renameFeed(@PathVariable Long feedId,
                                        @RequestBody JsonNode body) {
        subscriptionService.renameSubscription(feedId, body.textValue());
        return ResponseEntity.noContent().build();
    }
}
