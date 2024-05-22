package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntityCache;

import java.util.Optional;

public interface IUrlPersistenceCachePort {

    UrlEntityCache save(UrlEntityCache urlEntityCache);
    Optional<UrlEntityCache> findById(String id);
    void deleteById(String id);
    UrlEntityCache fallbackSave(UrlEntityCache urlEntityCache, Throwable t);
    Optional<UrlEntityCache> fallbackFindById(String id, Throwable t);
    void fallbackDeleteById(String id, Throwable t);

}
