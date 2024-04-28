package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlPersistenceCachePort;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.application.port.out.IUrlRepositoryCache;

import java.util.Optional;

@PersistenceAdapter
public class UrlPersistenceCacheAdapter implements IUrlPersistenceCachePort {

    private final IUrlRepositoryCache UrlRepositoryCache;

    public UrlPersistenceCacheAdapter(IUrlRepositoryCache urlRepositoryCache) {
        this.UrlRepositoryCache = urlRepositoryCache;
    }
   @Override
    public UrlCache save(UrlCache urlCache) {
      return UrlRepositoryCache.save(urlCache);

    }

    @Override
    public Optional<UrlCache> findById(String id) {
        return UrlRepositoryCache.findById(id);
    }

    @Override
    public void deleteById(String id) {
        UrlRepositoryCache.deleteById(id);
    }
}
