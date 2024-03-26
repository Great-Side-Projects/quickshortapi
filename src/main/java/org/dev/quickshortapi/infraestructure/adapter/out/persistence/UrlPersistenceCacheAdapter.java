package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlPersistenceCachePort;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.application.port.out.UrlRepositoryCache;

import java.util.Optional;

@PersistenceAdapter
public class UrlPersistenceCacheAdapter implements IUrlPersistenceCachePort {

    private final UrlRepositoryCache urlRepositoryCache;

    public UrlPersistenceCacheAdapter(UrlRepositoryCache urlRepositoryCache) {
        this.urlRepositoryCache = urlRepositoryCache;
    }
   @Override
    public UrlCache save(UrlCache urlCache) {
      return urlRepositoryCache.save(urlCache);

    }

    @Override
    public Optional<UrlCache> findById(String id) {
        return urlRepositoryCache.findById(id);
    }

    @Override
    public void deleteById(String id) {
        urlRepositoryCache.deleteById(id);
    }
}
