package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.dev.quickshortapi.application.port.out.IUrlPersistenceCachePort;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.application.port.out.IUrlRepositoryCache;
import org.slf4j.ILoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;

import java.util.Optional;
import java.util.logging.Logger;

@PersistenceAdapter
public class UrlPersistenceCacheAdapter implements IUrlPersistenceCachePort {

    private final IUrlRepositoryCache UrlRepositoryCache;
    Logger logger = Logger.getLogger(getClass().getName());

    public UrlPersistenceCacheAdapter(IUrlRepositoryCache urlRepositoryCache) {
        this.UrlRepositoryCache = urlRepositoryCache;
    }

    @Override
    @CircuitBreaker(name = "urlPersistenceCache", fallbackMethod = "fallbackSave")
    public UrlCache save(UrlCache urlCache) {
      return UrlRepositoryCache.save(urlCache);

    }

    @Override
    @CircuitBreaker(name = "urlPersistenceCache", fallbackMethod = "fallbackFindById")
    public Optional<UrlCache> findById(String id) {
        //throw new RedisConnectionFailureException("Forced error for testing Circuit Breaker");
        return UrlRepositoryCache.findById(id);
    }

    @Override
    @CircuitBreaker(name = "urlPersistenceCache", fallbackMethod = "fallbackDeleteById")
    public void deleteById(String id) {
        UrlRepositoryCache.deleteById(id);
    }

    public UrlCache fallbackSave(UrlCache urlCache, Throwable t) {
        // Implementar lógica de fallback aquí
        logger.info("Fallback method for save");
        return null; // o un valor por defecto
    }

    public Optional<UrlCache> fallbackFindById(String id, Throwable t) {
        // Implementar lógica de fallback aquí
        logger.info("Fallback method for findById");
        return Optional.empty(); // o un valor por defecto
    }

    public void fallbackDeleteById(String id, Throwable t) {
        // Implementar lógica de fallback aquí
        logger.info("Fallback method for deleteById");
    }
}
