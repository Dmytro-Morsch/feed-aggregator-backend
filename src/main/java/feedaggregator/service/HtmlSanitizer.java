package feedaggregator.service;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class HtmlSanitizer {

    public String sanitize(String rawHtml) {
        Safelist safelist = Safelist.basic()
                .addTags("img")
                .addAttributes("img", "src", "alt", "title", "width", "height");

        return Jsoup.clean(rawHtml, safelist);
    }
}
