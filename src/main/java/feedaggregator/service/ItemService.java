package feedaggregator.service;

import feedaggregator.module.Item;
import feedaggregator.module.ReadItem;
import feedaggregator.module.User;
import feedaggregator.repository.ItemRepository;
import feedaggregator.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public void markRead(Item item, boolean read) {
        ReadItem readItem = new ReadItem();
        readItem.setItem(item);
        User user = userRepository.getById(1L);
        readItem.setUser(user);
        if (read) {
            itemRepository.markRead(readItem);
        } else {
            itemRepository.markUnread(item.getId(), 1L);
        }
    }

    public void markAllRead(List<Long> itemIds) {
        for (Long itemId : itemIds) {
            markRead(itemRepository.findById(itemId), true);
        }
    }
}
