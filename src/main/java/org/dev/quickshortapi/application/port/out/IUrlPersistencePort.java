package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IUrlPersistencePort {

    String getShortUrlbyOriginalUrl(String originalUrl);

    boolean existCollisionbyShortUrl(String shortUrl);

    Url save(Url url);

    Optional<Url> getUrlOrThrowByShortUrl(String shortUrl);

    void increaseVisitsAndUpdateLastVisitedDate(Url url);

    boolean deleteUrlbyShortUrl(String shortUrl);

    Optional<UrlEntity> findByUShortUrl(String shortUrl);

    Page<UrlEntity> getAllUrls(int page, int pageSize);
}