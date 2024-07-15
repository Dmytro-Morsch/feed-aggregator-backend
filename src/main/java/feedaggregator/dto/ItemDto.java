package feedaggregator.dto;

import feedaggregator.module.Item;

import java.time.Instant;

public class ItemDto {
    public Long id;
    public String title;
    public String description;
    public String link;
    public Instant pubDate;
    public String guid;
    public boolean read;
    public boolean starred;

    public static ItemDto fromEntity(Item item, boolean read, boolean starred) {
        ItemDto itemDto = new ItemDto();
        itemDto.id = item.getId();
        itemDto.title = item.getTitle();
        itemDto.description = item.getDescription();
        itemDto.link = item.getLink();
        itemDto.pubDate = item.getPubDate();
        itemDto.guid = item.getGuid();
        itemDto.read = read;
        itemDto.starred = starred;
        return itemDto;
    }
}
