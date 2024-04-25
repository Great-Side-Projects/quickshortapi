package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;

import java.util.Optional;

public interface IUrlPersistencePort {

    String getShortUrlbyOriginalUrl(String urlOriginal);

    boolean existCollisionbyShortUrl(String urlCorta);

    Url save(Url url);

    Optional<Url> getUrlOrThrowByShortUrl(String urlCorta);

    void increaseVisits(Url url);

    boolean deleteUrlbyShortUrl(String urlCorta);

    Optional<UrlEntity> findByUrlCorta(String urlCorta);


}