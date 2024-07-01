package feedaggregator.module;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class ReadItemKey implements Serializable {
    @Column
    private Long userId;

    @Column
    private Long itemId;

    private ReadItemKey() {
    }

    public ReadItemKey(Long userId, Long itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }


    @Override
    public int hashCode() {
        return Objects.hash(userId, itemId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReadItemKey that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(itemId, that.itemId);
    }
}
