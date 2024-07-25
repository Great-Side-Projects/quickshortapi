package org.dev.quickshortapi.domain;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

public class Url {
    private String id;
    private String originalUrl;
    private String shortUrl;
    private Long visits = 0L;
    private Date createdDate;
    private Date lastVisitedDate;

    public Url() {
    }
    public Url(String originalUrl) {
        this.originalUrl = originalUrl;
        this.createdDate = new Date();
    }

    public Url(String id, String originalUrl, String shortUrl) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Long getVisits() {
        return visits;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastVisitedDate() {
        return lastVisitedDate;
    }
    public void setLastVisitedDate(Date lastVisitedDate) {
        this.lastVisitedDate = lastVisitedDate;
    }

    public boolean isValidUrl() {
        try {
            URI.create(originalUrl).toURL();
            return true;
        } catch (MalformedURLException | IllegalArgumentException e) {
            return false;
        }
    }
}