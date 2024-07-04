package feedaggregator.repository;

import feedaggregator.module.Feed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository
@Transactional
public class FeedRepository {
    private final EntityManager entityManager;

    public FeedRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Feed feed) {
        entityManager.persist(feed);
    }

    public Feed findById(Long id) {
        return entityManager.find(Feed.class, id);
    }

    public List<Feed> findAll() {
        Query query = entityManager.createQuery("from Feed");
        return query.getResultList();
    }

    public Feed findByFeedLink(String feedLink) {
        Query query = entityManager.createQuery("from Feed where feedLink = :feedLink");
        query.setParameter("feedLink", feedLink);
        return (Feed) DataAccessUtils.singleResult(query.getResultList());
    }

    public byte[] getIcon(Long feedId) {
        Query query = entityManager.createQuery("select icon from Feed where id = :id");
        query.setParameter("id", feedId);
        return (byte[]) query.getSingleResult();
    }

    public List<Long> findIds() {
        Query query = entityManager.createQuery("select id from Feed");
        return query.getResultList();
    }

}
