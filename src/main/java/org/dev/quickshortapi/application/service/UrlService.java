package org.dev.quickshortapi.application.service;

import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.port.out.*;
import org.dev.quickshortapi.application.port.in.shortener.IUrlShortener;
import org.dev.quickshortapi.common.UseCase;
import org.dev.quickshortapi.domain.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.domain.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.domain.Url;
import org.springframework.data.domain.Page;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@UseCase
public class UrlService implements IUrlServicePort {

    private IUrlShortener urlShortener;
    private IUrlPersistenceCachePort urlRepositoryCacheAdapter;
    private IUrlPersistencePort urlPersistenceAdapter;
    private IUrlEventStreamingPort urlEventStreamingAdapter;
    private static final String INVALID_URL = "URL no válida";

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

        logger.log(Level.INFO, "URL original: {0}", urlCommand.getUrl());

        Url url = new Url(urlCommand.getUrl());
        if (!url.isValidUrl()) {
            logger.warning(INVALID_URL);
            throw new UrlNotFoundException(INVALID_URL);
        }

        //Verificar si la URL original ya existe en la base de datos
        String shortUrlDB = urlPersistenceAdapter.getShortUrlbyOriginalUrl(url.getOriginalUrl());
        if (!shortUrlDB.isEmpty()) {
            logger.log(Level.INFO, "URL corta ya existe en la base de datos: {0}", shortUrlDB);
            return new UrlShortenResponse(url.getOriginalUrl(), shortUrlDB);
        }

        // Lógica para generar la URL corta
        String shortUrl = urlShortener.generateSHAShortUrl(url.getOriginalUrl());
        logger.log(Level.INFO, "URL corta generada SHA: {0}", shortUrl);

        if (urlPersistenceAdapter.existCollisionbyShortUrl(shortUrl)) {
            // Si la URL corta ya existe, genera otra URL corta Random
            shortUrl = urlShortener.generateRandomShortUrl();
            logger.log(Level.INFO, "URL corta generada Random: {0}", shortUrl);
        }
        url.setShortUrl(shortUrl);
        // Guardar en DB
        try {
            urlPersistenceAdapter.save(url);
            urlRepositoryCacheAdapter.save(url);
            logger.log(Level.INFO, "URL corta guardada: {0}", shortUrl);
            return new UrlShortenResponse(url.getOriginalUrl(), url.getShortUrl());
        } catch (Exception e) {
            throw new UrlInternalServerErrorException("Error interno al guardar la URL:" + e.getMessage());
        }
    }

    @Override
    public String redirectUrl(String shortUrl) {

        Optional<Url> url = urlRepositoryCacheAdapter.findById(shortUrl);

        if (!url.isEmpty() && !url.get().isValidUrl()){
            logger.warning(INVALID_URL);
            throw new UrlNotFoundException(INVALID_URL);
        }

        if (url.isEmpty()) {
            // Actualizar en cache si solo estaba en la base de datos
            url = urlPersistenceAdapter.getUrlOrThrowByShortUrl(shortUrl).map(u -> {
                urlRepositoryCacheAdapter.save(u);
                return u;
            });
        }
        urlEventStreamingAdapter.sendVisitedEvent(url.get());
        return url.get().getOriginalUrl();
    }

    @Override
    public void deleteUrlbyShortUrl(String shortUrl) {

        if (urlPersistenceAdapter.deleteUrlbyShortUrl(shortUrl)) {
            urlRepositoryCacheAdapter.deleteById(shortUrl);
            logger.log(Level.INFO,"URL corta eliminada: {0}" , shortUrl);
            return;
        }
        throw new UrlNotFoundException("URL corta no encontrada");
    }

    @Override
    public UrlStatisticsResponse getUrlStatistics(String shortUrl) {
        Optional<UrlStatisticsResponse> urlStatisticsResponse = urlPersistenceAdapter.getStatisticsByShortUrl(shortUrl);
        if (urlStatisticsResponse.isPresent()) {
            return urlStatisticsResponse.get();
        }
        throw new UrlNotFoundException("URL corta no encontrada");
    }

    @Override
    public void deleteCachebyShortUrl(String shortUrl) {
        urlRepositoryCacheAdapter.deleteById(shortUrl);
        logger.log(Level.INFO,"Cache eliminado: {0}" , shortUrl);
    }

    @Override
    public Page<UrlResponse> getAllUrls(int page, int pageSize) {
        return urlPersistenceAdapter.getAllUrls(page, pageSize);
    }
}