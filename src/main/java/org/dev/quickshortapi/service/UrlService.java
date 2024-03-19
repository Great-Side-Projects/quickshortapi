package org.dev.quickshortapi.service;

import org.dev.quickshortapi.common.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.repository.UrlRepository;
import org.dev.quickshortapi.repository.data.Url;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private UrlRepository urlRepository;
    private IUrlShortener urlShortenerService;

    public UrlService(UrlRepository urlRepository , IUrlShortener urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
        this.urlRepository = urlRepository;
    }

    public String acortarURL(String urlOriginal) {

        System.out.println("URL original: " + urlOriginal);

        if (!urlShortenerService.isValidUrl(urlOriginal)) {
            // Manejar el caso de URL no válida
            System.out.println("URL no válida");
            throw new UrlNotFoundException("URL no válida");
        }
        // Verificar si la URL original ya existe en la base de datos
        Url existenteoriginal = urlRepository.findByUrlOriginal(urlOriginal);
        if (existenteoriginal != null) {
            System.out.println("URL corta existente findByUrlOriginal: " + existenteoriginal.getUrlCorta());
            return existenteoriginal.getUrlCorta(); // Devolver la URL corta existente si ya está en la base de datos
        }
        // Lógica para generar la URL corta
        String urlCorta =  urlShortenerService.generarURLCortaSHA(urlOriginal);
        System.out.println("URL corta generada: " + urlCorta);

        // Verificar si la URL corta ya existe en la base de datos
        while (urlRepository.findByUrlCorta(urlCorta) != null) {
            System.out.println("URL corta existente findByUrlCorta: " + urlCorta);
            // Si la URL corta ya existe, genera otra URL corta
            urlCorta = urlShortenerService.generarURLCortaRandom(urlOriginal);
            System.out.println("URL corta generada generarURLCorta_Random: " + urlCorta);
        }
        // Guardar en MongoDB
        Url nuevaURL = new Url(urlOriginal, urlCorta);
        urlRepository.save(nuevaURL);

        System.out.println("URL corta guardada: " + urlCorta);
        return urlCorta;
    }

    public String redirigirURL(String urlCorta) {
        Url url = urlRepository.findByUrlCorta(urlCorta);
        if (url != null) {
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
}