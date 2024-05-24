package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.application.port.out.IUrlPersistenceCachePort;
import org.dev.quickshortapi.application.port.out.IUrlPersistenceFallBackCachePort;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.application.port.out.IUrlRepositoryCache;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.domain.event.cache.UrlDeleteByIdCacheEvent;
import org.dev.quickshortapi.domain.event.cache.UrlFindByIdCacheEvent;
import org.dev.quickshortapi.domain.event.cache.UrlSaveCacheEvent;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@PersistenceAdapter
public class UrlPersistenceCacheAdapter implements IUrlPersistenceCachePort, IUrlPersistenceFallBackCachePort {

    private final IUrlRepositoryCache urlRepositoryCache;
    private final IUrlEventTemplatePort<String> urlEventRabbitMQTemplateAdapter;
    Logger logger = Logger.getLogger(getClass().getName());
    private static final String MESSAGE_ERROR = "Error sending event to RabbitMQ {0}";

    public UrlPersistenceCacheAdapter(IUrlRepositoryCache urlRepositoryCache, IUrlEventTemplatePort<String> urlEventRabbitMQTemplateAdapter) {
        this.urlRepositoryCache = urlRepositoryCache;
        this.urlEventRabbitMQTemplateAdapter = urlEventRabbitMQTemplateAdapter;
    }

    @Override
    @CircuitBreaker(name = "urlPersistenceCache", fallbackMethod = "fallbackSave")
    public Url save(Url url) {
        return UrlMapper.toUrl(urlRepositoryCache.save(UrlMapper.toUrlEntityCache(url)));
    }

    @Override
    @CircuitBreaker(name = "urlPersistenceCache", fallbackMethod = "fallbackFindById")
    public Optional<Url> findById(String id) {
        return urlRepositoryCache.findById(id).map(UrlMapper::toUrl);
    }

    @Override
    @CircuitBreaker(name = "urlPersistenceCache", fallbackMethod = "fallbackDeleteById")
    public void deleteById(String id) {
        urlRepositoryCache.deleteById(id);
        logger.log(Level.INFO,"URL corta eliminada: {0}" , id);
    }

    @Override
    public Url fallbackSave(Url url, Throwable t) {

        logger.log(Level.SEVERE, "Error saving urlCache: {0}", t.getMessage());
        try {
            UrlSaveCacheEvent urlSaveCacheEvent = UrlMapper.toUrlSaveCacheEvent(UrlMapper.toUrlEventCache(url));
            String eventString = String.format("Error saving urlCache: %s, Event: %s", t.getMessage(), urlSaveCacheEvent);
            urlEventRabbitMQTemplateAdapter.send(eventString);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, MESSAGE_ERROR, e.getMessage());
        }
        return null; // o un valor por defecto
    }

    @Override
    public Optional<Url> fallbackFindById(String id, Throwable t) {

        logger.log(Level.SEVERE, "Error finding urlCache by id: {0}", t.getMessage());
        try {
            UrlFindByIdCacheEvent urlFindByIdCacheEvent = UrlMapper.toUrlFindByIdCacheEvent(id);
            String eventString = String.format("Error finding urlCache by id: %s, Event: %s", t.getMessage(), urlFindByIdCacheEvent);
            urlEventRabbitMQTemplateAdapter.send(eventString);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, MESSAGE_ERROR, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void fallbackDeleteById(String id, Throwable t) {

        logger.log(Level.SEVERE, "Error deleting urlCache by id: {0}", t.getMessage());
        try {
            UrlDeleteByIdCacheEvent urlDeleteByIdCacheEvent = UrlMapper.toUrlDeleteByIdCacheEvent(id);
            String eventString = String.format("Error deleting urlCache by id: %s, Event: %s", t.getMessage(), urlDeleteByIdCacheEvent);
            urlEventRabbitMQTemplateAdapter.send(eventString);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, MESSAGE_ERROR, e.getMessage());
        }
    }
}
