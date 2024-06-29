package feedaggregator.repository;

import feedaggregator.module.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserRepository {

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User getById(Long id) {
        Query query = entityManager.createQuery("from User where id = :id");
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

}
