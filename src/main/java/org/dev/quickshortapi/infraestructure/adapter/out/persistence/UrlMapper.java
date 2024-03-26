package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.domain.Url;

public class UrlMapper {
    public static Url toUrl(UrlEntity urlEntity) {
        return new Url(urlEntity.getId(), urlEntity.getUrlOriginal(), urlEntity.getUrlCorta(), urlEntity.getVisitas());
    }

    public static UrlEntity toUrlEntity(Url url) {
        return new UrlEntity(url.getId(),url.getUrlOriginal(), url.getUrlCorta(), url.getVisitas());
    }

    public static Url toUrl(UrlCache urlCache) {
        return new Url(urlCache.getIdDbUrl(), urlCache.getUrlOriginal() ,urlCache.getId(), 0L);
    }

    public static UrlCache toUrlCache(Url url) {
        return new UrlCache(url.getUrlCorta(), url.getId(), url.getUrlOriginal());
    }
}
