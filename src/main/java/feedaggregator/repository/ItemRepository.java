package feedaggregator.repository;

import feedaggregator.module.Item;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

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
}
