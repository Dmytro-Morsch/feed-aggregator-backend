package feedaggregator;

import feedaggregator.module.Item;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RssParserTest {
    RssParser rssParser = new RssParser();

    @Test
    void parse() throws IOException, ParserConfigurationException, SAXException {
        try (FileInputStream inputStream = new FileInputStream("src/test/resources/feed.xml")) {
            List<Item> items = rssParser.parse(inputStream);

            assertEquals(25 , items.size());

            Item firstItem = items.get(0);
            assertEquals("Java Developer в Yael Acceptic, Київ, Харків, Варшава (Польща), віддалено", firstItem.getTitle());
            assertNotNull(firstItem.getDescription());
            assertEquals("https://jobs.dou.ua/companies/yael-acceptic/vacancies/271136/?utm_source=jobsrss", firstItem.getLink());
        }
    }
}