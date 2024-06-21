package feedaggregator.controller;

import feedaggregator.module.Feed;
import feedaggregator.repository.FeedRepository;
import feedaggregator.service.FeedDownloaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FeedController {
    @Autowired
    private FeedDownloaderService feedDownloaderService;

    @Autowired
    private FeedRepository feedRepository;

    @PostMapping("/api/feed-link")
    public ResponseEntity<?> postFeed(@RequestBody String feedLink) {
        Feed feed = feedDownloaderService.asyncDownloadFeed(feedLink);
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
}
