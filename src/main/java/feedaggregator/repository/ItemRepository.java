package feedaggregator.repository;

import feedaggregator.module.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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
        Query query = entityManager.createNativeQuery("""
                insert into Item (title, link, pub_date, description, feed_id, guid, read) values (
                :title,
                :link,
                :pubDate,
                :description,
                :feed,
                :guid,
                :read) on conflict (guid) do nothing
                """);
        query.setParameter("title", item.getTitle());
        query.setParameter("link", item.getLink());
        query.setParameter("pubDate", item.getPubDate());
        query.setParameter("description", item.getDescription());
        query.setParameter("feed", item.getFeed().getId());
        query.setParameter("guid", item.getGuid());
        query.setParameter("read", item.isRead());
        query.executeUpdate();
    }

    public Item findById(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll(boolean isDescOrder, boolean isUnreadPosts) {
        StringBuilder sqlQuery = new StringBuilder("from Item");
        if (isUnreadPosts) sqlQuery.append(" where read = false");
        sqlQuery.append(" order by pubDate");
        if (isDescOrder) sqlQuery.append(" desc");
        Query query = entityManager.createQuery(sqlQuery.toString());
        return query.getResultList();
    }

    public List<Item> findByFeedId(Long feedId, boolean isDescOrder, boolean isUnreadPosts) {
        StringBuilder sqlQuery = new StringBuilder("from Item where feed.id = :feedId");
        if (isUnreadPosts) sqlQuery.append(" and read = false");
        sqlQuery.append(" order by pubDate");
        if (isDescOrder) sqlQuery.append(" desc");
        Query query = entityManager.createQuery(sqlQuery.toString());
        query.setParameter("feedId", feedId);
        return query.getResultList();
    }

    public void markRead(List<Long> itemIds) {
        Query query = entityManager.createQuery("""
                update Item
                set read = true
                where id in :ids
                """);
        query.setParameter("ids", itemIds);
        query.executeUpdate();
    }

    public Item findByGuid(String guid) {
        Query query = entityManager.createQuery("from Item where guid = :guid");
        query.setParameter("guid", guid);
        return (Item) query.getSingleResult();
    }
}
