package feedaggregator.repository;

import feedaggregator.module.Item;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testSaveAndGet() {
        Item item = new Item();
        item.setTitle("Test title");
        item.setDescription("Test description");
        item.setLink("Test link");
        itemRepository.save(item);

        Item loadedItem = itemRepository.findById(item.getId());
        assertEquals(item.getTitle(), loadedItem.getTitle());
    }
}