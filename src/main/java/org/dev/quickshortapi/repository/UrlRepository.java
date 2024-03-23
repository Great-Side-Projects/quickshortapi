package org.dev.quickshortapi.repository;

import org.dev.quickshortapi.repository.data.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends MongoRepository<Url, String> {
    Optional<Url> findByUrlCorta(String urlCorta);
    Optional<Url> findByUrlOriginal(String urlOriginal);
    void deleteByUrlCorta(String urlCorta);













}
