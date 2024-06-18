package feedaggregator.controller;

import feedaggregator.repository.FeedRepository;
import feedaggregator.service.FeedDownloaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {
    @Autowired
    private FeedDownloaderService feedDownloaderService;

    @Autowired
    private FeedRepository feedRepository;

    @PostMapping("/api/feed-link")
    public ResponseEntity<?> postFeed(@RequestBody String feedLink) {
        feedDownloaderService.asyncDownloadFeed(feedLink);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/feeds")
    public ResponseEntity<?> getFeeds() {
        return ResponseEntity.ok(feedRepository.findAll());
    }
}
