package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlProjection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IUrlRepository extends MongoRepository<UrlEntity, String> {

    @Query(value="{ 'originalUrl' : ?0 }", fields="{ 'shortUrl' : 1, '_id' : 0 }")
    Optional<UrlEntity> findOriginalUrlByShortUrl(String shortUrl);
    void deleteByShortUrl(String urlCorta);
    @Query(value = "{ 'shortUrl': ?0 }", fields = "{ 'id': 1, 'originalUrl': 1, 'shortUrl': 1 }")
    Optional<UrlProjection> findByShortUrlProjection(String urlCorta);
    boolean existsByShortUrl(String shortUrl);
    @Query(value="{ 'shortUrl' : ?0 }", fields="{ 'visits' : 1, 'createdDate' : 1, 'lastVisitedDate' : 1, '_id' : 0 }")
    Optional<UrlEntity> findUrlStatisticsByShortUrl(String shortUrl);
}
