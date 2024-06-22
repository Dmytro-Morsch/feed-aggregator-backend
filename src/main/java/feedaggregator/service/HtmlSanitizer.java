package feedaggregator.service;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class HtmlSanitizer {

    public String sanitize(String rawHtml) {
        return Jsoup.clean(rawHtml, Safelist.basic());
    }
}
