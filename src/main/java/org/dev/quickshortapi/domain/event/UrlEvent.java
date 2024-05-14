package org.dev.quickshortapi.domain.event;

import lombok.Data;
import java.util.Date;

@Data
public class UrlEvent {
    private String id;
    private String originalUrl;
    private String shortUrl;
    private Date lastVisitedDate;

    public UrlEvent(String id, String originalUrl, String shortUrl, Date lastVisitedDate) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.lastVisitedDate = lastVisitedDate;
    }
    public UrlEvent() {
    }

}
