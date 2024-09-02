package feedaggregator.repository;

import feedaggregator.module.*;
import feedaggregator.util.Crypto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private Feed feed;

    private User user;

    @BeforeEach
    void setUp() {
        feed = new Feed();
        feed.setTitle("Test title");
        feed.setDescription("Test description");
        feed.setFeedLink("Test feed link");
        feed.setSiteLink("Test site link");
        feedRepository.save(feed);

        user = new User();
        user.setUsername("Test");
        user.setEmail("test@example.com");
        user.setEmailHash(Crypto.sha256("test@example.com"));
        user.setPassword("secret");
        userRepository.save(user);

        createItems();

        Subscription subscription = new Subscription();
        subscription.setFeed(feed);
        subscription.setUser(user);
        subscriptionRepository.subscribe(subscription);
    }

    @Test
    void testSaveAndGetAll() {
        List<UserItem> userItems = itemRepository.getUserItems(user.getId(), feed.getId(), false, false, false);

        assertEquals(5, userItems.size());
        assertEquals("Test title 1", userItems.get(0).item().getTitle());
        assertEquals("Test title 5", userItems.get(4).item().getTitle());
    }

    @Test
    void testSaveAndGetUnreadOnly() {
        List<UserItem> userItems = itemRepository.getUserItems(user.getId(), feed.getId(), false, true, false);

        assertEquals(3, userItems.size());
        assertEquals("Test title 1", userItems.get(0).item().getTitle());
        assertEquals("Test title 2", userItems.get(1).item().getTitle());
        assertEquals("Test title 3", userItems.get(2).item().getTitle());
    }

    private void createItems() {
        Item item1 = new Item();
        item1.setTitle("Test title 1");
        item1.setDescription("Test description 1");
        item1.setLink("Test link 1");
        item1.setPubDate(Instant.now());
        item1.setGuid(UUID.randomUUID().toString());
        item1.setFeed(feed);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setTitle("Test title 2");
        item2.setDescription("Test description 2");
        item2.setLink("Test link 2");
        item2.setPubDate(Instant.now());
        item2.setGuid(UUID.randomUUID().toString());
        item2.setFeed(feed);
        itemRepository.save(item2);

        Item item3 = new Item();
        item3.setTitle("Test title 3");
        item3.setDescription("Test description 3");
        item3.setLink("Test link 3");
        item3.setPubDate(Instant.now());
        item3.setGuid(UUID.randomUUID().toString());
        item3.setFeed(feed);
        itemRepository.save(item3);

        Item item4 = new Item();
        item4.setTitle("Test title 4");
        item4.setDescription("Test description 4");
        item4.setLink("Test link 4");
        item4.setPubDate(Instant.now());
        item4.setGuid(UUID.randomUUID().toString());
        item4.setFeed(feed);
        itemRepository.save(item4);
        ReadItem readItem4 = new ReadItem();
        readItem4.setUser(user);
        readItem4.setItem(item4);
        itemRepository.markRead(readItem4);

        Item item5 = new Item();
        item5.setTitle("Test title 5");
        item5.setDescription("Test description 5");
        item5.setLink("Test link 5");
        item5.setPubDate(Instant.now());
        item5.setGuid(UUID.randomUUID().toString());
        item5.setFeed(feed);
        itemRepository.save(item5);
        ReadItem readItem5 = new ReadItem();
        readItem5.setUser(user);
        readItem5.setItem(item5);
        itemRepository.markRead(readItem5);
    }
}