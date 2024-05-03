package org.dev.quickshortapi.application.service;

import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.port.out.*;
import org.dev.quickshortapi.common.format.IUrlFormat;
import org.dev.quickshortapi.common.format.UrlFormatProvider;
import org.dev.quickshortapi.common.shortener.IUrlShortener;
import org.dev.quickshortapi.common.UseCase;
import org.dev.quickshortapi.common.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.common.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlMapper;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlCache;
import org.springframework.data.domain.Page;
import java.util.Optional;
import java.util.logging.Logger;

@UseCase
public class UrlService implements IUrlServicePort {

    private IUrlShortener urlShortener;
    private IUrlPersistenceCachePort urlRepositoryCacheAdapter;
    private IUrlPersistencePort urlPersistenceAdapter;
    private IUrlEventStreamingPort urlEventStreamingAdapter;

    Logger logger = Logger.getLogger(getClass().getName());

    public UrlService(IUrlPersistencePort urlRepository,
                      IUrlShortener urlShortener,
                      IUrlPersistenceCachePort urlRepositoryCache,
                      IUrlEventStreamingPort urlEventStreaming) {
        this.urlShortener = urlShortener;
        this.urlRepositoryCacheAdapter = urlRepositoryCache;
        this.urlPersistenceAdapter = urlRepository;
        this.urlEventStreamingAdapter = urlEventStreaming;
    }
    @Override
    public UrlShortenResponse shortenUrl(UrlCommand urlCommand) {

        logger.info("URL original: " + urlCommand.getUrl());

        Url url = new Url(urlCommand.getUrl());
        if (!url.isValidUrl()) {
            logger.warning("URL no válida");
            throw new UrlNotFoundException("URL no válida");
        }

        //Verificar si la URL original ya existe en la base de datos
        String shortUrlDB = urlPersistenceAdapter.getShortUrlbyOriginalUrl(url.getOriginalUrl());
        if (!shortUrlDB.isEmpty()) {
            logger.info("URL existente findByUrlOriginal: " + shortUrlDB);
            return new UrlShortenResponse(url.getOriginalUrl(), shortUrlDB);
        }

        // Lógica para generar la URL corta
        String shortUrl = urlShortener.generateSHAShortUrl(url.getOriginalUrl());
        logger.info("URL corta generada SHA: " + shortUrl);

        if (urlPersistenceAdapter.existCollisionbyShortUrl(shortUrl)) {
            // Si la URL corta ya existe, genera otra URL corta Random
            shortUrl = urlShortener.generateRandomShortUrl(url.getOriginalUrl());
            logger.info("URL corta generada Random: " + shortUrl);
        }
        url.setShortUrl(shortUrl);
        // Guardar en DB
        try{
            urlPersistenceAdapter.save(url);
            urlRepositoryCacheAdapter.save(UrlMapper.toUrlCache(url));
            logger.info("URL corta guardada: " + shortUrl);
        return new UrlShortenResponse(url.getOriginalUrl(), url.getShortUrl());
        }
        catch (Exception e) {
            logger.severe("Error interno al guardar la URL:" + e.getMessage());
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
            logger.info("URL guardada en cache redirectUrl: " + urlCacheAux.getId());
        }
        else {
            url = Optional.of(UrlMapper.toUrl(urlCache.get()));
        }

        if (!url.get().isValidUrl()) {
            logger.warning("URL no válida");
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
    public UrlStatisticsResponse getUrlStatistics(String shortUrl) {
        Optional<UrlEntity> url = urlPersistenceAdapter.findByUShortUrl(shortUrl);
        if (url.isPresent()) {
            return UrlMapper.toUrlStatisticsResponse(url.get(), new UrlFormatProvider());
        }
        throw new UrlNotFoundException("URL corta no encontrada");
    }

    @Override
    public void deleteCachebyShortUrl(String shortUrl) {
        urlRepositoryCacheAdapter.deleteById(shortUrl);
        logger.info("Cache eliminado: " + shortUrl);
    }

    @Override
    public Page<UrlResponse> getAllUrls(int page) {
        Page<UrlEntity> urls = urlPersistenceAdapter.getAllUrls(page);
        IUrlFormat urlFormatProvider = new UrlFormatProvider();
        return urls.map(urlEntity -> UrlMapper.toUrlResponse(urlEntity, urlFormatProvider));


    }
}