package feedaggregator.module;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class SubscriptionKey implements Serializable {
    @Column
    private Long userId;

    @Column
    private Long feedId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, feedId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionKey that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(feedId, that.feedId);
    }
}
