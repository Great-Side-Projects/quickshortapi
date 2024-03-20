package org.dev.quickshortapi.service;

import org.dev.quickshortapi.common.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.repository.UrlRepository;
import org.dev.quickshortapi.repository.data.Url;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UrlService {

    private UrlRepository urlRepository;
    private IUrlShortener urlShortenerService;

    public UrlService(UrlRepository urlRepository, IUrlShortener urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
        this.urlRepository = urlRepository;
    }

    public String acortarURL(String urlOriginal) {

        System.out.println("URL original: " + urlOriginal);
        validarFormatoUrl(urlOriginal);

        //Verificar si la URL original ya existe en la base de datos
        Url existenteoriginal = urlRepository.findByUrlOriginal(urlOriginal);
        if (existenteoriginal != null) {
            System.out.println("URL corta existente findByUrlOriginal: " + existenteoriginal.getUrlCorta());
            return existenteoriginal.getUrlCorta(); // Devolver la URL corta existente si ya está en la base de datos
        }
        // Lógica para generar la URL corta
        String urlCorta = urlShortenerService.generarURLCortaSHA(urlOriginal);
        System.out.println("URL corta generada SHA: " + urlCorta);

        urlCorta = validarColisionesUrlCorta(urlOriginal, urlCorta);
        // Guardar en MongoDB
        Url nuevaURL = new Url(urlOriginal, urlCorta);
        urlRepository.save(nuevaURL);
        System.out.println("URL corta guardada: " + urlCorta);
        return urlCorta;
    }

    private String validarColisionesUrlCorta(String urlOriginal, String urlCorta) {
        // Verificar si la URL corta ya existe en la base de datos
        if (urlRepository.findByUrlCorta(urlCorta) != null) {
            System.out.println("URL corta existente findByUrlCorta: " + urlCorta);
            // Si la URL corta ya existe, genera otra URL corta Random
            urlCorta = urlShortenerService.generarURLCortaRandom(urlOriginal);
            System.out.println("URL corta generada generarURLCorta_Random: " + urlCorta);
        }
        return urlCorta;
    }

    private void validarFormatoUrl(String urlOriginal) {
        if (!urlShortenerService.isValidUrl(urlOriginal)) {
            // Manejar el caso de URL no válida
            System.out.println("URL no válida");
            throw new UrlNotFoundException("URL no válida");
        }
    }

    public String redirigirURL(String urlCorta) {
        Url url = urlRepository.findByUrlCorta(urlCorta);
        if (url != null) {
            validarFormatoUrl(url.getUrlOriginal());
            //Todo: find a way to update the visits without using the save method from the repository
            incrementarVisitasAndSave(url);
            return url.getUrlOriginal();
        } else {
            // Manejar el caso de URL corta no encontrada
            throw new UrlNotFoundException("URL corta no encontrada");
        }
    }

    public void deleteUrlbyUrlCorta(String urlCorta) {

        if (urlRepository.findByUrlCorta(urlCorta) != null) {
            urlRepository.deleteByUrlCorta(urlCorta);
        } else {
            // Manejar el caso de URL corta no encontrada
            throw new UrlNotFoundException("URL corta no encontrada");
        }
    }

    public Long estadisticasURL(String urlCorta) {
        Url url = urlRepository.findByUrlCorta(urlCorta);
        if (url != null) {
            return url.getVisitas();
        } else {
            // Manejar el caso de URL corta no encontrada
            throw new UrlNotFoundException("URL corta no encontrada");
        }
    }

    @Async
    public CompletableFuture<Url> incrementarVisitasAndSave(Url url) {
        url.setVisitas(url.getVisitas() + 1);
        return CompletableFuture.completedFuture(urlRepository.save(url));
    }
}