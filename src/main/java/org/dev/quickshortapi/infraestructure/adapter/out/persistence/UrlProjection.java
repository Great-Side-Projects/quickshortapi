package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

public interface UrlProjection {
    String getId();
    String getOriginalUrl();
    String getShortUrl();
}
