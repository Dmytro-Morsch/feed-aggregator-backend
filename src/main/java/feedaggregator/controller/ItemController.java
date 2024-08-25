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
                                      @RequestParam(required = false) boolean unreadOnly,
                                      @RequestParam(required = false) boolean starOnly,
                                      @RequestAttribute Long userId) {
        List<UserItem> userItems = itemRepository.getUserItems(userId, feedId, descOrder, unreadOnly, starOnly);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (UserItem userItem : userItems) {
            itemDtos.add(ItemDto.fromEntity(userItem.item(), userItem.item().getFeed().getId(), userItem.read(), userItem.starred()));
        }
        return ResponseEntity.ok(itemDtos);
    }

    @PatchMapping("/api/items/{id}/read")
    public ResponseEntity<?> markItemRead(@PathVariable Long id,
                                          @RequestBody Boolean read,
                                          @RequestAttribute Long userId) {
        Item item = itemRepository.findById(id);
        if (item == null) return ResponseEntity.notFound().build();
        itemService.markRead(item, read, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/items/all/read")
    public ResponseEntity<?> markAllRead(@RequestBody List<Long> itemIds,
                                         @RequestAttribute Long userId) {
        itemService.markAllRead(itemIds, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/items/{id}/star")
    public ResponseEntity<?> markItemStar(@PathVariable Long id,
                                          @RequestBody Boolean star,
                                          @RequestAttribute Long userId) {
        Item item = itemRepository.findById(id);
        if (item == null) return ResponseEntity.notFound().build();
        itemService.markStar(item, star, userId);
        return ResponseEntity.noContent().build();
    }
}
