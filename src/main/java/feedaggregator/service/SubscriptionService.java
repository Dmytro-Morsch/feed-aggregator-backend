package feedaggregator.service;

import feedaggregator.module.Feed;
import feedaggregator.module.Subscription;
import feedaggregator.module.User;
import feedaggregator.repository.FeedRepository;
import feedaggregator.repository.SubscriptionRepository;
import feedaggregator.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedRepository feedRepository;

    public Subscription subscribe(String feedLink, Long userId) {
        Feed feed = feedRepository.findByFeedLink(feedLink);
        if (feed == null) {
            feed = new Feed();
            feed.setFeedLink(feedLink);
            feed.setSiteLink(feedLink);
            feed.setTitle(feedLink);
            feedRepository.save(feed);
        }

        User user = userRepository.getById(userId);
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setFeed(feed);
        subscriptionRepository.subscribe(subscription);
        return subscription;
    }

    public void renameSubscription(Long feedId, String title, Long userId) {
        subscriptionRepository.rename(feedId, userId, title);
    }
}
