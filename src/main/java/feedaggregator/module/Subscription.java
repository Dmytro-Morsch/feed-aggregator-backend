package feedaggregator.module;

import jakarta.persistence.*;

@Entity
@IdClass(SubscriptionKey.class)
public class Subscription {
    @Id
    private Long feedId;

    @Id
    private Long userId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("feedId")
    @JoinColumn(name = "feed_id")
    private Feed feed;

    public Subscription() {
    }

    public Subscription(Long feedId, Long userId, Feed feed, User user) {
        this.feedId = feedId;
        this.userId = userId;
        this.user = user;
        this.feed = feed;
    }
}
