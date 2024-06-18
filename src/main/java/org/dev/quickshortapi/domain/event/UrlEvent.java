package org.dev.quickshortapi.domain.event;

import java.util.Date;

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
    public String toString() {
        return this.getClass().getName()+"{" +
                "id='" + id + '\'' +
                ", originalUrl=" + originalUrl +
                ", shortUrl=" + shortUrl +
                ", lastVisitedDate=" + lastVisitedDate +
                '}';
    }

    public String getId() {
        return id;
    }

    public Date getLastVisitedDate() {
        return lastVisitedDate;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
