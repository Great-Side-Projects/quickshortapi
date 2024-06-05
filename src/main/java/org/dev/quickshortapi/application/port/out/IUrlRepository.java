package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.Optional;

@Repository
public interface IUrlRepository extends MongoRepository<UrlEntity, String> {

    @Query(value = "{ 'shortUrl': ?0 }", fields = "{ 'originalUrl': 1, '_id': 1, 'shortUrl': 1 }")
    Optional<UrlEntity> findOriginalUrlByShortUrl(String shortUrl);
    @Query(value = "{ 'originalUrl': ?0 }", fields = "{ 'shortUrl': 1 }")
    Optional<UrlEntity> findShortUrlByOriginalUrl(String originalUrl);
    void deleteByShortUrl(String urlCorta);
    boolean existsByShortUrl(String shortUrl);
    @Query(value="{ 'shortUrl' : ?0 }", fields="{ 'visits' : 1, 'createdDate' : 1, 'lastVisitedDate' : 1, '_id' : 0 }")
    Optional<UrlEntity> findUrlStatisticsByShortUrl(String shortUrl);
    @Query(value = "{ '_id' : ?0 }")
    @Update(update ="{'$inc': {'visits': ?1}, '$set': {'lastVisitedDate': ?2}}")
    void incrementVisitsAndUpdateLastVisitedDateById(String id, long visits, Date lastVisitedDate);
}