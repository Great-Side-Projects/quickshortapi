package org.dev.quickshortapi.infraestructure.adapter.in.web;

import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.common.WebAdapter;
import org.dev.quickshortapi.domain.UrlEstadisticasResponse;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.service.UrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@WebAdapter
@RestController
@RequestMapping("/url")
public class UrlController {

    private final IUrlServicePort urlService;

    UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public String shorten(@RequestBody UrlCommand urlCommand) {
        return urlService.shortenUrl(urlCommand);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity redirect(@PathVariable String shortUrl) {
        //Todo: effiecient way to redirect?
        String urlOriginal = urlService.redirectUrl(shortUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(urlOriginal));
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/statistics/{shortUrl}")
    public UrlEstadisticasResponse statistics(@PathVariable String shortUrl) {
        return urlService.getUrlStatistics(shortUrl);
    }
    @DeleteMapping("/{shortUrl}")
    public void delete(@PathVariable String shortUrl) {
        urlService.deleteUrlbyShortUrl(shortUrl);
    }

    @DeleteMapping("/cache/{shortUrl}")
    public void deleteCache(@PathVariable String shortUrl) {
        urlService.deleteCachebyShortUrl(shortUrl);
    }
}
