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
        entityManager.persist(item);
    }

    public Item findById(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll() {
        Query query = entityManager.createQuery("from Item");
        return query.getResultList();
    }

    public List<Item> findByFeedId(Long feedId) {
        Query query = entityManager.createQuery("from Item where feedId = :feedId");
        query.setParameter("feedId", feedId);
        return query.getResultList();
    }
}
