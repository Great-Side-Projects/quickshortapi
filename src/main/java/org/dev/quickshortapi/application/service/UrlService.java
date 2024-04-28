package org.dev.quickshortapi.application.service;

import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.port.out.IUrlEventStreamingPort;
import org.dev.quickshortapi.application.port.out.IUrlPersistenceCachePort;
import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
import org.dev.quickshortapi.application.port.out.IUrlShortenerPort;
import org.dev.quickshortapi.common.UseCase;
import org.dev.quickshortapi.common.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.common.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.domain.UrlEstadisticasResponse;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlMapper;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlCache;
import java.util.Optional;

@UseCase
public class UrlService implements IUrlServicePort {

    private IUrlShortenerPort urlShortenerService;
    private IUrlPersistenceCachePort urlRepositoryCacheAdapter;
    private IUrlPersistencePort urlPersistenceAdapter;
    private IUrlEventStreamingPort urlEventStreamingAdapter;

    public UrlService(IUrlPersistencePort urlRepository,
                      IUrlShortenerPort urlShortenerService,
                      IUrlPersistenceCachePort urlRepositoryCache,
                      IUrlEventStreamingPort urlEventStreaming) {
        this.urlShortenerService = urlShortenerService;
        this.urlRepositoryCacheAdapter = urlRepositoryCache;
        this.urlPersistenceAdapter = urlRepository;
        this.urlEventStreamingAdapter = urlEventStreaming;
    }
    @Override
    public String shortenUrl(UrlCommand urlCommand) {

        System.out.println("URL original: " + urlCommand.getUrl());

        Url url = new Url(urlCommand.getUrl());
        if (!url.isValidUrl()) {
            System.out.println("URL no válida");
            throw new UrlNotFoundException("URL no válida");
        }

        //Verificar si la URL original ya existe en la base de datos
        String shortUrlDB = urlPersistenceAdapter.getShortUrlbyOriginalUrl(url.getOriginalUrl());
        if (!shortUrlDB.isEmpty()) {
            return shortUrlDB;
        }

        // Lógica para generar la URL corta
        String shortUrl = urlShortenerService.generateSHAShortUrl(url.getOriginalUrl());
        System.out.println("URL corta generada SHA: " + shortUrl);

        if (urlPersistenceAdapter.existCollisionbyShortUrl(shortUrl)) {
            // Si la URL corta ya existe, genera otra URL corta Random
            shortUrl = urlShortenerService.generateRandomShortUrl(url.getOriginalUrl());
            System.out.println("URL corta generada generateRandomShortUrl: " + shortUrl);
        }
        url.setShortUrl(shortUrl);
        // Guardar en DB
        try{
            urlPersistenceAdapter.save(url);
            urlRepositoryCacheAdapter.save(UrlMapper.toUrlCache(url));
            System.out.println("URL corta guardada: " + shortUrl);
        return shortUrl;
        }
        catch (Exception e) {
            System.out.println("Error interno al guardar la URL:" + e.getMessage());
            throw new UrlInternalServerErrorException("Error interno al guardar la URL:" + e.getMessage());
        }
    }

    @Override
    public String redirectUrl(String shortUrl) {

        Optional<UrlCache> urlCache = urlRepositoryCacheAdapter.findById(shortUrl);
        Optional<Url> url;
        if (urlCache.isEmpty()) {
            url =  urlPersistenceAdapter.getUrlOrThrowByShortUrl(shortUrl);
            // Actualizar en cache si solo estaba en la base de datos
            UrlCache urlCacheAux = urlRepositoryCacheAdapter.save(UrlMapper.toUrlCache(url.get()));
            System.out.println("URL guardada en cache redirectUrl: " + urlCacheAux.getId());
        }
        else {
            url = Optional.of(UrlMapper.toUrl(urlCache.get()));
        }

        if (!url.get().isValidUrl()) {
            System.out.println("URL no válida");
            throw new UrlNotFoundException("URL no válida");
        }

        urlEventStreamingAdapter.sendVisitedEvent(url.get());
        return url.get().getOriginalUrl();
    }

    @Override
    public void deleteUrlbyShortUrl(String shortUrl) {

        if (urlPersistenceAdapter.deleteUrlbyShortUrl(shortUrl)) {
            urlRepositoryCacheAdapter.deleteById(shortUrl);
            return;
        }
        throw new UrlNotFoundException("URL corta no encontrada");
    }

    @Override
    public UrlEstadisticasResponse getUrlStatistics(String shortUrl) {
        Optional<UrlEntity> url = urlPersistenceAdapter.findByUShortUrl(shortUrl);
        if (url.isPresent()) {
            return new UrlEstadisticasResponse(url.get().getVisits());
        }
        throw new UrlNotFoundException("URL corta no encontrada");
    }

    @Override
    public void deleteCachebyShortUrl(String shortUrl) {
        urlRepositoryCacheAdapter.deleteById(shortUrl);
        System.out.println("Cache eliminado: " + shortUrl);
    }
}