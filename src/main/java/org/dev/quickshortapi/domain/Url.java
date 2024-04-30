package org.dev.quickshortapi.domain;

import lombok.Data;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

@Data
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

    public boolean isValidUrl() {
        //Todo: improve URL validation
        try {
            new URL(this.originalUrl).toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}