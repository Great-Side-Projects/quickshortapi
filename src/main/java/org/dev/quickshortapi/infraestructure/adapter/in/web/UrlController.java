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

    @PostMapping("/acortar")
    public String acortar(@RequestBody UrlCommand urlCommand) {
        return urlService.acortarURL(urlCommand);
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
