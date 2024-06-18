package feedaggregator.repository;

import feedaggregator.module.Feed;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class FeedRepositoryTest {
    @Autowired
    private FeedRepository feedRepository;

    @Test
    void testSaveAndGet() {
        Feed feed = new Feed();
        feed.setTitle("Test title");
        feed.setSiteLink("Test siteLink");
        feed.setFeedLink("Test feedLink");
        feed.setDescription("Test description");
        feedRepository.save(feed);

        Feed loadedFeed = feedRepository.findById(feed.getId());

        assertEquals(feed.getTitle(), loadedFeed.getTitle());
    }

    @Test
    void testGetList() {
        Feed feed1 = new Feed();
        feed1.setTitle("Test first title");
        feed1.setSiteLink("Test first siteLink");
        feed1.setFeedLink("Test first feedLink");
        feed1.setDescription("Test first description");

        Feed feed2 = new Feed();
        feed2.setTitle("Test second title");
        feed2.setSiteLink("Test second siteLink");
        feed2.setFeedLink("Test second feedLink");
        feed2.setDescription("Test second description");

        Feed feed3 = new Feed();
        feed3.setTitle("Test third title");
        feed3.setSiteLink("Test third siteLink");
        feed3.setFeedLink("Test third feedLink");
        feed3.setDescription("Test third description");
        feedRepository.save(feed1);
        feedRepository.save(feed2);
        feedRepository.save(feed3);

        List<Feed> feeds = feedRepository.findAll();

        assertEquals(3, feeds.size());
        assertEquals("Test second title", feeds.get(1).getTitle());
    }
}