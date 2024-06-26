package feedaggregator.repository;

import feedaggregator.module.Subscription;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository
@Transactional
public class SubscriptionRepository {

    @Autowired
    private EntityManager entityManager;

    public void subscribe(Subscription subscription) {
        entityManager.persist(subscription);
    }

    public List<Subscription> getSubscriptions(Long userId) {
        Query query = entityManager.createQuery("from Subscription where user.id = :userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public void unsubscribe(Long feedId, Long userId) {
        Query query = entityManager.createQuery("""
                delete from Subscription where feed.id = :feedId and user.id = :userId
                """);
        query.setParameter("feedId", feedId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    public void rename(Long feedId, Long userId, String title) {
        Query query = entityManager.createQuery("""
                update Subscription
                set title = :title
                where userId = :userId and feedId = :feedId
                """);
        query.setParameter("title", title);
        query.setParameter("userId", userId);
        query.setParameter("feedId", feedId);
        query.executeUpdate();
    }
}
