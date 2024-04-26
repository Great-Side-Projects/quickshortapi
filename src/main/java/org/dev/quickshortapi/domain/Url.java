package org.dev.quickshortapi.domain;

import lombok.Data;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Data
public class Url {
    private String id;
    private String originalUrl;
    private String shortUrl;
    private Long visits = 0L;

    public Url(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Url(String id, String originalUrl, String shortUrl, Long visits) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.visits = visits;
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
