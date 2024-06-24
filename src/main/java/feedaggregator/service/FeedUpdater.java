package feedaggregator.service;

import feedaggregator.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Service
public class FeedUpdater {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedDownloader feedDownloader;

    @Scheduled(fixedDelay = 1_800_000)
    private void updateFeeds() throws IOException, ParserConfigurationException, InterruptedException, SAXException {
        List<Long> feedIds = feedRepository.findIds();
        for (Long feedId : feedIds) {
            feedDownloader.downloadFeed(feedId);
        }
    }
}
