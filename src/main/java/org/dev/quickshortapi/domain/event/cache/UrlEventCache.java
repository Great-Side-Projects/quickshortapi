package org.dev.quickshortapi.domain.event.cache;

import lombok.Data;

@Data
public class UrlEventCache {
    private String id;
    private String idDbUrl;
    private String originalUrl;

    public UrlEventCache(String id, String idDbUrl, String originalUrl) {
        this.id = id;
        this.idDbUrl = idDbUrl;
        this.originalUrl = originalUrl;
    }
    public UrlEventCache() {
    }

    public String toString() {
        return "UrlEventCache{" +
                "id='" + id + '\'' +
                ", idDbUrl='" + idDbUrl + '\'' +
                ", originalUrl='" + originalUrl + '\'' +
                '}';
    }
}
