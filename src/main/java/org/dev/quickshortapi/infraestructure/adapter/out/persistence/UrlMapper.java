package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.common.event.EventType;
import org.dev.quickshortapi.common.event.UrlVisitedEvent;
import org.dev.quickshortapi.domain.Url;
import java.util.Date;
import java.util.UUID;

public class UrlMapper {
    public static Url toUrl(UrlEntity urlEntity) {
        return new Url(urlEntity.getId(), urlEntity.getOriginalUrl(), urlEntity.getShortUrl(), urlEntity.getVisits());
    }

    public static UrlEntity toUrlEntity(Url url) {
        return new UrlEntity(url.getOriginalUrl(), url.getShortUrl(), url.getVisits(), url.getCreatedDate());
    }

    public static Url toUrl(UrlCache urlCache) {
        return new Url(urlCache.getIdDbUrl(), urlCache.getOriginalUrl() ,urlCache.getId(), 0L);
    }

    public static UrlCache toUrlCache(Url url) {
        return new UrlCache(url.getShortUrl(), url.getId(), url.getOriginalUrl());
    }

    public static UrlVisitedEvent toUrlVisitedEvent(Url url){
        return new UrlVisitedEvent(UUID.randomUUID().toString(),new Date(), EventType.VISITED, url);
    }
}
