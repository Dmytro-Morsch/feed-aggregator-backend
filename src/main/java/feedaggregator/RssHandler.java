package feedaggregator;

import feedaggregator.module.Feed;
import feedaggregator.module.Item;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RssHandler extends DefaultHandler {
    private final StringBuilder text = new StringBuilder();
    private Item item;
    private final Feed feed;
    private final List<Item> items = new ArrayList<>();

    public RssHandler(Feed feed) {
        this.feed = feed;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        text.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        text.setLength(0);

        if (qName.equals("item")) {
            item = new Item();
            items.add(item);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (item == null) {
            switch (qName) {
                case "title" -> feed.setTitle(text.toString());
                case "description" -> feed.setDescription(text.toString());
                case "link" -> feed.setSiteLink(text.toString());
            }
        } else {
            switch (qName) {
                case "title" -> item.setTitle(text.toString());
                case "description" -> item.setDescription(text.toString());
                case "link" -> item.setLink(text.toString());
                case "pubDate" -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss [Z][z]");
                    Instant instant = ZonedDateTime.parse(text.toString(), formatter).toInstant();
                    item.setPubDate(instant);
                }
            }
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public Feed getFeed() {
        return feed;
    }
}
