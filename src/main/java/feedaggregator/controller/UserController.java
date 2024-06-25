package feedaggregator.controller;

import feedaggregator.module.Feed;
import feedaggregator.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private FeedRepository feedRepository;

    @GetMapping("/api/user/subscriptions")
    public ResponseEntity<?> getSubscription() {
        List<Feed> userFeeds = feedRepository.getUserFeeds(1L);
        return ResponseEntity.ok(userFeeds);
    }

}
