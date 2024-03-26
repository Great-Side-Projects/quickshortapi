package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlCache;

import java.util.Optional;

public interface IUrlPersistenceCachePort {

    UrlCache save(UrlCache urlCache);
    Optional<UrlCache> findById(String id);
    void deleteById(String id);

}
