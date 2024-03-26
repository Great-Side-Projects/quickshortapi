package org.dev.quickshortapi.application.service;

import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.application.port.in.UrlCommand;
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
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@UseCase
public class UrlService implements IUrlServicePort {

    private IUrlShortenerPort urlShortenerService;
    private IUrlPersistenceCachePort urlRepositoryCacheAdapter;
    private IUrlPersistencePort urlPersistenceAdapter;

    public UrlService(IUrlPersistencePort urlRepository,
                      IUrlShortenerPort urlShortenerService,
                      IUrlPersistenceCachePort urlRepositoryCache) {
        this.urlShortenerService = urlShortenerService;
        this.urlRepositoryCacheAdapter = urlRepositoryCache;
        this.urlPersistenceAdapter = urlRepository;
    }
    @Override
    public String acortarURL(UrlCommand urlCommand) {

        System.out.println("URL original: " + urlCommand.getUrl());

        Url url = new Url(urlCommand.getUrl());
        if (!url.isValidUrl()) {
            System.out.println("URL no válida");
            throw new UrlNotFoundException("URL no válida");
        }

        //Verificar si la URL original ya existe en la base de datos
        String urlCortaDB = urlPersistenceAdapter.getShortUrlbyOriginalUrl(url.getUrlOriginal());
        if (!urlCortaDB.isEmpty()) {
            return urlCortaDB;
        }

        // Lógica para generar la URL corta
        String urlCorta = urlShortenerService.generarURLCortaSHA(url.getUrlOriginal());
        System.out.println("URL corta generada SHA: " + urlCorta);

        if (urlPersistenceAdapter.existCollisionbyShortUrl(urlCorta)) {
            // Si la URL corta ya existe, genera otra URL corta Random
            urlCorta = urlShortenerService.generarURLCortaRandom(url.getUrlOriginal());
            System.out.println("URL corta generada generarURLCorta_Random: " + urlCorta);
        }

        url.setUrlCorta(urlCorta);

        // Guardar en DB

        try{
            urlPersistenceAdapter.save(url);
            // Todo: save in cache
            urlRepositoryCacheAdapter.save(UrlMapper.toUrlCache(url));
            System.out.println("URL corta guardada: " + urlCorta);
        return urlCorta;
        }
        catch (Exception e) {
            System.out.println("Error interno al guardar la URL:" + e.getMessage());
            throw new UrlInternalServerErrorException("Error interno al guardar la URL:" + e.getMessage());
        }
    }

    @Override
    public String redirigirURL(String urlCorta) {

        Optional<UrlCache> urlCache = urlRepositoryCacheAdapter.findById(urlCorta);
        Optional<Url> url;
        if (urlCache.isEmpty()) {
            url =  urlPersistenceAdapter.getUrlAndThrowByShortUrl(urlCorta);
            // Guardar en cache si solo estaba en la base de datos
            UrlCache urlCacheAux = urlRepositoryCacheAdapter.save(UrlMapper.toUrlCache(url.get()));
            System.out.println("URL guardada en cache redirigirURL: " + urlCacheAux.getId());
            url = Optional.of(UrlMapper.toUrl(urlCacheAux));
        }
        else {
            url = Optional.of(UrlMapper.toUrl(urlCache.get()));
        }

        if (!url.get().isValidUrl()) {
            System.out.println("URL no válida");
            throw new UrlNotFoundException("URL no válida");
        }

        Optional<Url> finalUrl = url;
        CompletableFuture.runAsync(() -> incrementarVisitas(finalUrl));
        //incrementarVisitas(finalUrl);
        return url.get().getUrlOriginal();
    }

    @Override
    public void deleteUrlbyUrlCorta(String urlCorta) {

        if (urlPersistenceAdapter.deleteUrlbyShortUrl(urlCorta)) {
            urlRepositoryCacheAdapter.deleteById(urlCorta);
            return;
        }
        throw new UrlNotFoundException("URL corta no encontrada");
    }

    @Override
    public UrlEstadisticasResponse estadisticasURL(String urlCorta) {
        Optional<UrlEntity> url = urlPersistenceAdapter.findByUrlCorta(urlCorta);
        if (url.isPresent()) {
            UrlCache urlCache = urlRepositoryCacheAdapter.findById(urlCorta)
                    .orElse(new UrlCache());
            return new UrlEstadisticasResponse(url.get().getVisitas());
        }
        throw new UrlNotFoundException("URL corta no encontrada");
    }

    @Async
    protected CompletableFuture<Long> incrementarVisitas(Optional<Url> url) {
        urlPersistenceAdapter.increaseVisits(url.get());
        return CompletableFuture.completedFuture(url.get().getVisitas());
    }

    @Override
    public void deleteCachebyUrlCorta(String urlCorta) {
        urlRepositoryCacheAdapter.deleteById(urlCorta);
        System.out.println("Cache eliminado: " + urlCorta);
    }
}