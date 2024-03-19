package org.dev.quickshortapi.controller;

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
        String urlOriginal = urlService.redirigirURL(urlCorta);
        URI uri = URI.create(urlOriginal);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @DeleteMapping("/{urlCorta}")
    public void eliminar(@PathVariable UrlRequest urlCorta) {
        urlService.deleteUrlbyUrlCorta(urlCorta.getUrl());
    }
}
