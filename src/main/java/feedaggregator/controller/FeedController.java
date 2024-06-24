package feedaggregator.controller;

import feedaggregator.module.Feed;
import feedaggregator.module.Item;
import feedaggregator.repository.FeedRepository;
import feedaggregator.repository.ItemRepository;
import feedaggregator.service.FeedDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@RestController
public class FeedController {
    @Autowired
    private FeedDownloader feedDownloader;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/api/feed-link")
    public ResponseEntity<?> postFeed(@RequestBody String feedLink) {
        Feed feed = feedDownloader.asyncDownloadFeed(feedLink);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/api/feeds")
    public ResponseEntity<?> getFeeds() {
        return ResponseEntity.ok(feedRepository.findAll());
    }

    @GetMapping("/api/feeds/{id}/icon")
    public ResponseEntity<?> getFeedIcon(@PathVariable Long id) {
        byte[] icon = feedRepository.getIcon(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf("image/x-icon")).body(icon);
    }

    @PostMapping("/api/feeds/{id}/update")
    public ResponseEntity<?> updateFeed(@PathVariable Long id,
                                        @RequestParam boolean isDescOrder) throws IOException, ParserConfigurationException, InterruptedException, SAXException {
        feedDownloader.downloadFeed(id);
        List<Item> items = itemRepository.findByFeedId(id, isDescOrder, false);
        return ResponseEntity.ok(items);
    }
}
