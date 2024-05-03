package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.UrlResponse;
import org.dev.quickshortapi.application.port.out.UrlStatisticsResponse;
import org.dev.quickshortapi.common.event.EventType;
import org.dev.quickshortapi.common.event.UrlVisitedEvent;
import org.dev.quickshortapi.common.format.IUrlFormat;
import org.dev.quickshortapi.domain.Url;
import java.util.Date;
import java.util.UUID;

public class UrlMapper {

    private UrlMapper() {
    }
    public static Url toUrl(UrlEntity urlEntity) {
        return new Url(urlEntity.getId(), urlEntity.getOriginalUrl(), urlEntity.getShortUrl());
    }
    public static UrlResponse toUrlResponse(UrlEntity urlEntity, IUrlFormat urlFormatProvider) {
        return new UrlResponse(urlEntity.getId(),
                urlEntity.getOriginalUrl(),
                urlEntity.getShortUrl(),
                urlEntity.getVisits(),
                urlEntity.getCreatedDate(),
                urlEntity.getLastVisitedDate(),
                urlFormatProvider);
    }

    public static UrlStatisticsResponse toUrlStatisticsResponse(UrlEntity urlEntity, IUrlFormat urlFormatProvider) {
        return new UrlStatisticsResponse(urlEntity.getVisits(),
                urlEntity.getCreatedDate(),
                urlEntity.getLastVisitedDate(),
                urlFormatProvider);
    }
    public static UrlEntity toUrlEntity(Url url) {
        return new UrlEntity(url.getOriginalUrl(), url.getShortUrl(), url.getVisits(), url.getCreatedDate());
    }

    public static Url toUrl(UrlCache urlCache) {
        return new Url(urlCache.getIdDbUrl(), urlCache.getOriginalUrl() ,urlCache.getId());
    }

    public static UrlCache toUrlCache(Url url) {
        return new UrlCache(url.getShortUrl(), url.getId(), url.getOriginalUrl());
    }

    public static UrlVisitedEvent toUrlVisitedEvent(Url url){
        return new UrlVisitedEvent(UUID.randomUUID().toString(),new Date(), EventType.VISITED, url);
    }
}
