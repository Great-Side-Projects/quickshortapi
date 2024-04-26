package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.domain.Url;

public class UrlMapper {
    public static Url toUrl(UrlEntity urlEntity) {
        return new Url(urlEntity.getId(), urlEntity.getOriginalUrl(), urlEntity.getShortUrl(), urlEntity.getVisits());
    }

    public static UrlEntity toUrlEntity(Url url) {
        return new UrlEntity(url.getId(),url.getOriginalUrl(), url.getShortUrl(), url.getVisits());
    }

    public static Url toUrl(UrlCache urlCache) {
        return new Url(urlCache.getIdDbUrl(), urlCache.getOriginalUrl() ,urlCache.getId(), 0L);
    }

    public static UrlCache toUrlCache(Url url) {
        return new UrlCache(url.getShortUrl(), url.getId(), url.getOriginalUrl());
    }
}
