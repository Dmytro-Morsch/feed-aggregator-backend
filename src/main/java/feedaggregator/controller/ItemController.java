package feedaggregator.controller;

import feedaggregator.module.Item;
import feedaggregator.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/api/items")
    public ResponseEntity<?> getItems(@RequestParam(required = false) Long feedId,
                                      @RequestParam(required = false) boolean isDescOrder) {
        List<Item> items;
        if (feedId != null) {
            items = itemRepository.findByFeedId(feedId, isDescOrder);
        } else {
            items = itemRepository.findAll(isDescOrder);
        }
        return ResponseEntity.ok(items);
    }
}
