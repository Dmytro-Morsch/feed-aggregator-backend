package feedaggregator.dto;

import feedaggregator.module.Feed;
import feedaggregator.module.Subscription;

public class SubscriptionDto {
    public Long id;
    public String title;
    public String description;
    public String feedLink;
    public String siteLink;
    public boolean loaded;
    public Long countUnreadItems;

    public static SubscriptionDto fromEntity(Subscription subscription, Long countUnreadItems) {
        SubscriptionDto subDto = new SubscriptionDto();
        Feed feed = subscription.getFeed();
        subDto.id = feed.getId();
        if (subscription.getTitle() != null) {
            subDto.title = subscription.getTitle();
        } else {
            subDto.title = feed.getTitle();
        }
        subDto.description = feed.getDescription();
        subDto.feedLink = feed.getFeedLink();
        subDto.siteLink = feed.getSiteLink();
        subDto.loaded = feed.isLoaded();
        subDto.countUnreadItems = countUnreadItems;
        return subDto;
    }
}
