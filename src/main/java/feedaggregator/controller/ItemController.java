package feedaggregator.controller;

import feedaggregator.module.Item;
import feedaggregator.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/api/items")
    public ResponseEntity<?> getItems(@RequestParam(required = false) Long feedId,
                                      @RequestParam(required = false) boolean isDescOrder,
                                      @RequestParam(required = false) boolean isUnreadPosts) {
        List<Item> items;
        if (feedId != null) {
            items = itemRepository.findByFeedId(feedId, isDescOrder, isUnreadPosts);
        } else {
            items = itemRepository.findAll(isDescOrder, isUnreadPosts);
        }
        return ResponseEntity.ok(items);
    }

    @PatchMapping("/api/items/{id}")
    public ResponseEntity<?> markItemRead(@PathVariable Long id,
                                          @RequestBody boolean read) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        item.setRead(read);
        itemRepository.save(item);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/items/all/read")
    public ResponseEntity<?> markAllRead(@RequestBody List<Long> itemIds) {
        itemRepository.markRead(itemIds);
        return ResponseEntity.noContent().build();
    }
}
