package feedaggregator.repository;

import feedaggregator.module.Feed;
import feedaggregator.module.Item;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FeedRepository feedRepository;

    private Feed feed;

    @BeforeEach
    void setUp() {
        feed = new Feed();
        feed.setTitle("Test title");
        feed.setDescription("Test description");
        feed.setFeedLink("Test feed link");
        feed.setSiteLink("Test site link");
        feedRepository.save(feed);
    }

    @Test
    void testSaveAndGet() {
        Item item = new Item();
        item.setTitle("Test title");
        item.setDescription("Test description");
        item.setLink("Test link");
        item.setPubDate(Instant.now());
        item.setFeed(feed);
        itemRepository.save(item);

        Item loadedItem = itemRepository.findById(item.getId());
        assertEquals(item.getTitle(), loadedItem.getTitle());
    }
}