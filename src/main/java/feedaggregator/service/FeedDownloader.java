package feedaggregator.service;

import feedaggregator.RssParser;
import feedaggregator.module.Feed;
import feedaggregator.repository.FeedRepository;
import feedaggregator.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
public class FeedDownloader {
    private static final Logger log = LoggerFactory.getLogger(FeedDownloader.class);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private HtmlSanitizer htmlSanitizer;

    public void asyncDownloadFeed(Feed feed) {
        executorService.execute(() -> transactionTemplate.execute(status -> {
            try {
                downloadFeed(feed.getId());
            } catch (IOException | InterruptedException | ParserConfigurationException | SAXException e) {
                log.error("Download failed", e);
            }
            return null;
        }));
    }

    public void downloadFeed(Long feedId) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        Feed feed = feedRepository.findById(feedId);
        log.info("Downloading feed {}", feed.getFeedLink());
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(feed.getFeedLink()))
                .GET()
                .build();
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        ByteArrayInputStream bais = new ByteArrayInputStream(response.body());

        RssParser rssParser = new RssParser();
        RssParser.ParseResult result = rssParser.parse(bais, feed);

        feed.setIcon(downloadSiteIcon(feed.getSiteLink()));
        feed.setLoaded(true);
        feedRepository.save(feed);
        result.items().forEach(item -> {
            item.setFeed(feed);
            item.setTitle(htmlSanitizer.sanitize(item.getTitle()));
            item.setDescription(htmlSanitizer.sanitize(item.getDescription()));
            itemRepository.save(item);
        });
        log.info("Feed downloaded {}", feed.getFeedLink());
    }

    private byte[] downloadSiteIcon(String siteLink) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        URI uri = URI.create(siteLink);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri.getScheme() + "://" + uri.getHost() + "/favicon.ico"))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
    }
}
