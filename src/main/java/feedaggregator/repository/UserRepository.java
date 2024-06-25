package feedaggregator.repository;

import feedaggregator.module.Subscription;
import feedaggregator.module.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserRepository {

    @Autowired
    private EntityManager entityManager;

    public void subscribe(Subscription subscription) {
        entityManager.persist(subscription);
    }

    public void unsubscribe(Long feedId, Long userId) {
        Query query = entityManager.createQuery("""
                delete from Subscription where feed.id = :feedId and user.id = :userId
                """);
        query.setParameter("feedId", feedId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    public User getById(Long id) {
        Query query = entityManager.createQuery("from User where id = :id");
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

}
