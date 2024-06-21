package feedaggregator;

import feedaggregator.module.Feed;
import feedaggregator.module.Item;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RssParser {
    public record ParseResult(Feed feed, List<Item> items) {
    }

    public ParseResult parse(InputStream xml, Feed feed) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        RssHandler handler = new RssHandler(feed);
        saxParser.parse(xml, handler);

        return new ParseResult(handler.getFeed(), handler.getItems());
    }
}
