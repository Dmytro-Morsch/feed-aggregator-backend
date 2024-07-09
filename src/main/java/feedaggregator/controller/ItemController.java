package feedaggregator.controller;

import feedaggregator.dto.ItemDto;
import feedaggregator.module.Item;
import feedaggregator.module.UserItem;
import feedaggregator.repository.ItemRepository;
import feedaggregator.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @GetMapping("/api/items")
    public ResponseEntity<?> getItems(@RequestParam(required = false) Long feedId,
                                      @RequestParam(required = false) boolean descOrder,
                                      @RequestParam(required = false) boolean unreadOnly) {
        List<UserItem> userItems = itemRepository.getUserItems(1L, feedId, descOrder, unreadOnly);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (UserItem userItem : userItems) {
            itemDtos.add(ItemDto.fromEntity(userItem.item(), userItem.read()));
        }
        return ResponseEntity.ok(itemDtos);
    }

    @GetMapping("/api/feeds/{feedId}/items-count")
    public ResponseEntity<?> getItemsCount(@PathVariable Long feedId) {
        Long readItemsCount = itemRepository.getReadItemsCount(feedId, 1L);
        Long itemsCount = itemRepository.getItemsCount(feedId, 1L);
        return ResponseEntity.ok(itemsCount - readItemsCount);
    }

    @PatchMapping("/api/items/{id}")
    public ResponseEntity<?> markItemRead(@PathVariable Long id,
                                          @RequestBody Boolean read) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        itemService.markRead(item, read);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/items/all/read")
    public ResponseEntity<?> markAllRead(@RequestBody List<Long> itemIds) {
        itemService.markAllRead(itemIds);
        return ResponseEntity.noContent().build();
    }

}
