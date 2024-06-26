package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.UrlResponse;
import org.dev.quickshortapi.application.port.out.UrlStatisticsResponse;
import org.dev.quickshortapi.domain.event.*;
import org.dev.quickshortapi.application.port.format.IUrlFormatProviderPort;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.domain.event.cache.UrlDeleteByIdCacheEvent;
import org.dev.quickshortapi.domain.event.cache.UrlEventCache;
import org.dev.quickshortapi.domain.event.cache.UrlFindByIdCacheEvent;
import org.dev.quickshortapi.domain.event.cache.UrlSaveCacheEvent;
import java.util.Date;
import java.util.UUID;

public class UrlMapper {

    private UrlMapper() {
    }
    public static Url toUrl(UrlEntity urlEntity) {
        return new Url(urlEntity.getId(), urlEntity.getOriginalUrl(), urlEntity.getShortUrl());
    }

    public static UrlResponse toUrlResponse(UrlEntity urlEntity, IUrlFormatProviderPort urlFormatProviderAdapter) {
        return new UrlResponse(urlEntity.getId(),
                urlEntity.getOriginalUrl(),
                urlEntity.getShortUrl(),
                urlEntity.getVisits(),
                urlEntity.getCreatedDate(),
                urlEntity.getLastVisitedDate(),
                urlFormatProviderAdapter);
    }

    public static UrlStatisticsResponse toUrlStatisticsResponse(UrlEntity urlEntity, IUrlFormatProviderPort urlFormatProviderAdapter) {
        return new UrlStatisticsResponse(urlEntity.getVisits(),
                urlEntity.getCreatedDate(),
                urlEntity.getLastVisitedDate(),
                urlFormatProviderAdapter);
    }
    public static UrlEntity toUrlEntity(Url url) {
        return new UrlEntity(url.getOriginalUrl(), url.getShortUrl(), url.getVisits(), url.getCreatedDate());
    }

    public static Url toUrl(UrlEntityCache urlEntityCache) {
        return new Url(urlEntityCache.getIdDbUrl(), urlEntityCache.getOriginalUrl() , urlEntityCache.getId());
    }

    public static UrlEntityCache toUrlEntityCache(Url url) {
        return new UrlEntityCache(url.getShortUrl(), url.getId(), url.getOriginalUrl());
    }

    public static UrlVisitedEvent toUrlVisitedEvent(UrlEvent urlEvent){
        return new UrlVisitedEvent(UUID.randomUUID().toString(),new Date(), EventType.VISITED, urlEvent);
    }
    public static UrlEvent toUrlEvent(Url url){
        return new UrlEvent(url.getId(), url.getOriginalUrl(), url.getShortUrl(), url.getLastVisitedDate());
    }

    public static UrlSaveCacheEvent toUrlSaveCacheEvent(UrlEventCache urlEventCache){
        return new UrlSaveCacheEvent(UUID.randomUUID().toString(),new Date(), EventType.SAVE_CACHE, urlEventCache);
    }

    public static UrlFindByIdCacheEvent toUrlFindByIdCacheEvent(String data){
        return new UrlFindByIdCacheEvent(UUID.randomUUID().toString(),new Date(), EventType.FIND_BY_ID_CACHE, data);
    }

    public static UrlDeleteByIdCacheEvent toUrlDeleteByIdCacheEvent(String data){
        return new UrlDeleteByIdCacheEvent(UUID.randomUUID().toString(),new Date(), EventType.DELETE_BY_ID_CACHE, data);
    }

    public static UrlEventCache toUrlEventCache(Url url){
        return new UrlEventCache(url.getShortUrl(), url.getId(), url.getOriginalUrl());
    }
}
