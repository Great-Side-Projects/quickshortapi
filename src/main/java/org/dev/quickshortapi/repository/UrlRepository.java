package org.dev.quickshortapi.repository;

import org.dev.quickshortapi.repository.data.Url;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UrlRepository extends MongoRepository<Url, String> {
    Url findByUrlCorta(String urlCorta);
    Url findByUrlOriginal(String urlOriginal);
    void deleteByUrlCorta(String urlCorta);











}
