package feedaggregator.module;

import jakarta.persistence.*;

@Entity
@IdClass(SubscriptionKey.class)
public class Subscription {
    @Id
    private Long feedId;

    @Id
    private Long userId;

    private String title;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("feedId")
    @JoinColumn(name = "feed_id")
    private Feed feed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getId();
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
        this.feedId = feed.getId();
    }
}
