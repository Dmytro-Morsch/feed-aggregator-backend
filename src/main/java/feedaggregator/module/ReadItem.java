package feedaggregator.module;

import jakarta.persistence.*;

@Entity
@IdClass(ReadItemKey.class)
public class ReadItem {
    @Id
    private Long userId;

    @Id
    private Long itemId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getId();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        this.itemId = item.getId();
    }
}
