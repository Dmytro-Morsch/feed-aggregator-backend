package feedaggregator.repository;

import feedaggregator.module.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository
@Transactional
public class ItemRepository {
    private final EntityManager entityManager;

    public ItemRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Item item) {
        Item existingItem = findByGuid(item.getGuid());
        if (existingItem == null) {
            entityManager.persist(item);
        }
    }

    public Item findById(Long id) {
        return entityManager.find(Item.class, id);
    }

    public void markRead(ReadItem readItem) {
        entityManager.persist(readItem);
    }

    public void markUnread(Long itemId, Long userId) {
        ReadItem readItem = getReadItem(itemId, userId);
        if (readItem != null) entityManager.remove(readItem);
    }

    private ReadItem getReadItem(Long itemId, Long userId) {
        return entityManager.find(ReadItem.class, new ReadItemKey(userId, itemId));
    }

    public List<UserItem> getUserItems(Long userId, Long feedId, boolean descOrder, boolean unreadOnly, boolean starOnly) {
        StringBuilder sql = new StringBuilder("""
                select i as item, r.itemId is not null as read, si.itemId is not null as star from Item i
                inner join Subscription s on i.feed.id = s.feedId
                left outer join ReadItem r on i.id = r.itemId and s.userId = r.userId
                left outer join StarItem si on i.id = si.itemId and s.userId = si.userId
                where s.userId = :userId
                """);
        if (unreadOnly) sql.append(" and r.itemId is not null");
        if (starOnly) sql.append(" and si.itemId is not null");
        if (feedId != null) sql.append(" and s.feedId = :feedId");
        sql.append(" order by i.pubDate");
        if (descOrder) sql.append(" desc");
        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("userId", userId);
        if (feedId != null) query.setParameter("feedId", feedId);

        List<Object[]> resultList = query.getResultList();
        return resultList.stream().map(ItemRepository::toUserItem).toList();
    }

    public Item findByGuid(String guid) {
        Query query = entityManager.createQuery("from Item where guid = :guid");
        query.setParameter("guid", guid);
        return (Item) DataAccessUtils.singleResult(query.getResultList());
    }

    public Long getReadItemsCount(Long feedId, Long userId) {
        Query query = entityManager.createQuery("""
                select count(*)
                from Item i
                left outer join Subscription s on i.feed.id = s.feed.id
                left outer join ReadItem r on i.id = r.itemId and s.user.id = r.user.id
                where i.feed.id = :feedId and r.user.id = :userId
                """);
        query.setParameter("userId", userId);
        query.setParameter("feedId", feedId);
        return (Long) query.getSingleResult();
    }

    public Long getItemsCount(Long feedId, Long userId) {
        Query query = entityManager.createQuery("""
                select count(*)
                from Item i
                left outer join Subscription s on i.feed.id = s.feed.id
                where i.feed.id = :feedId and s.user.id = :userId
                """);
        query.setParameter("userId", userId);
        query.setParameter("feedId", feedId);
        return (Long) query.getSingleResult();
    }

    public void markStar(StarItem starItem) {
        entityManager.persist(starItem);
    }

    public void markUnstar(Long itemId, Long userId) {
        StarItem starItem = getStarItem(itemId, userId);
        if (starItem != null) entityManager.remove(starItem);
    }

    private StarItem getStarItem(Long itemId, Long userId) {
        return entityManager.find(StarItem.class, new StarItemKey(userId, itemId));
    }

    private static UserItem toUserItem(Object[] arr) {
        return new UserItem((Item) arr[0], (boolean) arr[1], (boolean) arr[2]);
    }
}
