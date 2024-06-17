package feedaggregator;

import feedaggregator.module.Item;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RssParser {
    public List<Item> parse(InputStream xml) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        RssHandler handler = new RssHandler();
        saxParser.parse(xml, handler);

        return handler.getItems();
    }
}
