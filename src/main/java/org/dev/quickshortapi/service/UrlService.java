package org.dev.quickshortapi.service;

import org.dev.quickshortapi.common.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.common.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.model.UrlEstadisticasResponse;
import org.dev.quickshortapi.repository.UrlRepository;
import org.dev.quickshortapi.repository.UrlRepositoryCache;
import org.dev.quickshortapi.repository.data.Url;
import org.dev.quickshortapi.repository.data.UrlCache;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UrlService {

    private UrlRepository urlRepository;
    private IUrlShortener urlShortenerService;
    private UrlRepositoryCache urlRepositoryCache;

    public UrlService(UrlRepository urlRepository,
                      IUrlShortener urlShortenerService,
                      UrlRepositoryCache urlRepositoryCache) {
        this.urlShortenerService = urlShortenerService;
        this.urlRepository = urlRepository;
        this.urlRepositoryCache = urlRepositoryCache;
    }

    public String acortarURL(String urlOriginal) {

        System.out.println("URL original: " + urlOriginal);
        validarFormatoUrl(urlOriginal);

        //Verificar si la URL original ya existe en la base de datos
        Optional<Url> existenteoriginal = urlRepository.findByUrlOriginal(urlOriginal);
        if (existenteoriginal.isPresent()) {
            System.out.println("URL corta existente findByUrlOriginal: " + existenteoriginal.get().getUrlCorta());
            return existenteoriginal.get().getUrlCorta(); // Devolver la URL corta existente si ya está en la base de datos
        }
        // Lógica para generar la URL corta
        String urlCorta = urlShortenerService.generarURLCortaSHA(urlOriginal);
        System.out.println("URL corta generada SHA: " + urlCorta);

        if (ExisteColisionUrlCorta(urlCorta)) {
            // Si la URL corta ya existe, genera otra URL corta Random
            urlCorta = urlShortenerService.generarURLCortaRandom(urlOriginal);
            System.out.println("URL corta generada generarURLCorta_Random: " + urlCorta);
        }
        // Guardar en DB
        Url nuevaURL = new Url(urlOriginal, urlCorta);
        try{
            urlRepository.save(nuevaURL);
            urlRepositoryCache.save(nuevaURL.toUrlCache());
            System.out.println("URL corta guardada: " + urlCorta);
        return urlCorta;
        }
        catch (Exception e) {
            throw new UrlInternalServerErrorException("Error interno al guardar la URL:" + e.getMessage());
        }
    }

    private boolean ExisteColisionUrlCorta(String urlCorta) {
        // Verificar si la URL corta ya existe en la base de datos
        if (urlRepository.findByUrlCorta(urlCorta).isPresent())
            return true;

        return false;
    }

    private void validarFormatoUrl(String urlOriginal) {
        if (!urlShortenerService.isValidUrl(urlOriginal)) {
            System.out.println("URL no válida");
            throw new UrlNotFoundException("URL no válida");
        }
    }

    public String redirigirURL(String urlCorta) {

        Optional<UrlCache> urlCache = urlRepositoryCache.findById(urlCorta);
        Optional<Url> url;
        if (urlCache.isEmpty()) {
            url = BuscarEnUrlRepositoryThrow(urlCorta);
            // Guardar en cache si solo estaba en la base de datos
            UrlCache urlCacheAux = urlRepositoryCache.save(url.get().toUrlCache());
            url = Optional.of(urlCacheAux.totUrl());
        }
        else {
            url = Optional.of(urlCache.get().totUrl());
        }
        validarFormatoUrl(url.get().getUrlOriginal());
        // Incrementar visitas async
        Optional<Url> finalUrl = url;
        CompletableFuture.runAsync(() -> incrementarVisitas(finalUrl));
        return url.get().getUrlOriginal();
    }

    private Optional<Url> BuscarEnUrlRepositoryThrow(String urlCorta) {
        Optional<Url> url = urlRepository.findByUrlCorta(urlCorta);
        if(url.isEmpty())
            throw new UrlNotFoundException("URL corta no encontrada");
        return url;
    }

    public void deleteUrlbyUrlCorta(String urlCorta) {

        if (urlRepository.findByUrlCorta(urlCorta).isPresent()) {
            urlRepository.deleteByUrlCorta(urlCorta);
            urlRepositoryCache.deleteById(urlCorta);
            return;
        }
        throw new UrlNotFoundException("URL corta no encontrada");
    }

    public UrlEstadisticasResponse estadisticasURL(String urlCorta) {
        Optional<Url> url = urlRepository.findByUrlCorta(urlCorta);
        if (url.isPresent()) {
            UrlCache urlCache = urlRepositoryCache.findById(urlCorta).orElse(new UrlCache());
            return new UrlEstadisticasResponse(url.get().getVisitas(), urlCache.getVisitas());
        }
        throw new UrlNotFoundException("URL corta no encontrada");
    }

    @Async
    public CompletableFuture<Long> incrementarVisitas(Optional<Url> url) {
        //Todo: find a way to update the visits without using the save method from the repository
        Url urlAux = url.get();
        try{
            urlAux.setVisitas(urlAux.getVisitas() + 1);
            urlRepository.save(urlAux);
            urlRepositoryCache.save(urlAux.toUrlCache());
        }
        catch (Exception e) {
            throw new UrlInternalServerErrorException("Error interno al incrementar las visitas:" + e.getMessage());
        }
        return CompletableFuture.completedFuture(urlAux.getVisitas());
    }
}