package org.dev.quickshortapi.controller;

import org.dev.quickshortapi.model.UrlEstadisticasResponse;
import org.dev.quickshortapi.model.UrlRequest;
import org.dev.quickshortapi.service.UrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/url")
public class UrlController {

    private UrlService urlService;

    UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/acortar")
    public String acortar(@RequestBody UrlRequest urlRequest) {
        return urlService.acortarURL(urlRequest.getUrl());
    }

    @GetMapping("/{urlCorta}")
    public ResponseEntity redirigir(@PathVariable String urlCorta) {
        //Todo: effiecient way to redirect?
        String urlOriginal = urlService.redirigirURL(urlCorta);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(urlOriginal));
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/estadisticas/{urlCorta}")
    public UrlEstadisticasResponse estadisticas(@PathVariable String urlCorta) {
        return urlService.estadisticasURL(urlCorta);
    }
    @DeleteMapping("/{urlCorta}")
    public void eliminar(@PathVariable String urlCorta) {
        urlService.deleteUrlbyUrlCorta(urlCorta);
    }

    @DeleteMapping("/cache/{urlCorta}")
    public void eliminarCache(@PathVariable String urlCorta) {
        urlService.deleteCachebyUrlCorta(urlCorta);
    }
}
