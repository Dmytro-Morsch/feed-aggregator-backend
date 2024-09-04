package feedaggregator.repository;

import feedaggregator.module.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserRepository {

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User findById(Long id) {
        Query query = entityManager.createQuery("from User where id = :id");
        query.setParameter("id", id);
        return (User) DataAccessUtils.singleResult(query.getResultList());
    }

    public User findByEmailHash(String emailHash) {
        Query query = entityManager.createQuery("from User where emailHash = :emailHash");
        query.setParameter("emailHash", emailHash);
        return (User) DataAccessUtils.singleResult(query.getResultList());
    }

    public void save(User user) {
        entityManager.persist(user);
    }

    public void update(User user) {
        Query query = entityManager.createQuery("""
                update User
                set email = :email,
                emailHash = :emailHash,
                username = :username,
                password = :password
                where id = :id
                """);
        query.setParameter("email", user.getEmail());
        query.setParameter("emailHash", user.getEmailHash());
        query.setParameter("username", user.getUsername());
        query.setParameter("password", user.getPassword());
        query.setParameter("id", user.getId());
        query.executeUpdate();
    }

    public void delete(User user) {
        entityManager.remove(user);
    }
}
