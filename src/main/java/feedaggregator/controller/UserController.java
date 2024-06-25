package feedaggregator.controller;

import feedaggregator.module.Feed;
import feedaggregator.repository.FeedRepository;
import feedaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/user/subscriptions")
    public ResponseEntity<?> getSubscription() {
        List<Feed> userFeeds = feedRepository.getUserFeeds(1L);
        return ResponseEntity.ok(userFeeds);
    }

    @DeleteMapping("/api/user/feed/{feedId}/unsubscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable Long feedId) {
        userRepository.unsubscribe(feedId, 1L);
        return ResponseEntity.ok().build();
    }

}
