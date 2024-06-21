package feedaggregator.service;

import feedaggregator.RssParser;
import feedaggregator.module.Feed;
import feedaggregator.repository.FeedRepository;
import feedaggregator.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class FeedDownloaderService {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FeedRepository feedRepository;

    public void asyncDownloadFeed(String feedUrl) {
        executorService.execute(() -> {
            try {
                downloadFeed(feedUrl);
            } catch (IOException | InterruptedException | ParserConfigurationException | SAXException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void downloadFeed(String feedUrl) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(feedUrl))
                .GET()
                .build();
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        ByteArrayInputStream bais = new ByteArrayInputStream(response.body());

        RssParser rssParser = new RssParser();
        RssParser.ParseResult result = rssParser.parse(bais);

        Feed feed = result.feed();
        feed.setFeedLink(feedUrl);
        feed.setIcon(downloadSiteIcon(feed.getSiteLink()));

        feedRepository.save(feed);
        result.items().forEach(item -> {
            item.setFeed(feed);
            itemRepository.save(item);
        });
    }

    private byte[] downloadSiteIcon(String siteLink) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(siteLink + "/favicon.ico"))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
    }
}
