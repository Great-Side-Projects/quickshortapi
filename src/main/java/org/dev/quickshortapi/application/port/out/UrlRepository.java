package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UrlRepository extends MongoRepository<UrlEntity, String> {

    Optional<UrlEntity> findByShortUrl(String urlCorta);
    Optional<UrlEntity> findByOriginalUrl(String urlOriginal);
    void deleteByShortUrl(String urlCorta);
}
