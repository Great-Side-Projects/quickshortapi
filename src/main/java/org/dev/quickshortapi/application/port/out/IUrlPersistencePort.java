package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.domain.event.UrlEvent;
import org.dev.quickshortapi.domain.Url;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface IUrlPersistencePort {

    String getShortUrlbyOriginalUrl(String originalUrl);

    boolean existCollisionbyShortUrl(String shortUrl);

    Url save(Url url);

    Optional<Url> getUrlOrThrowByShortUrl(String shortUrl);

    void increaseVisitsAndUpdateLastVisitedDate(UrlEvent urlEvent);

    boolean deleteUrlbyShortUrl(String shortUrl);

    Optional<Url> findByUShortUrl(String shortUrl);

    Optional<UrlStatisticsResponse> getStatisticsByShortUrl(String shortUrl);

    Page<UrlResponse> getAllUrls(int page, int pageSize);
}