package org.dev.quickshortapi.infraestructure.adapter.in;

import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.port.out.UrlShortenResponse;
import org.dev.quickshortapi.application.service.UrlService;
import org.dev.quickshortapi.infraestructure.adapter.in.web.UrlController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UrlControllerTests {

    private UrlController urlController;
    private IUrlServicePort urlService;

    @BeforeEach
    void setup() {
        urlService = Mockito.mock(UrlService.class);
        urlController = new UrlController((UrlService) urlService);
    }

    @Test
    void shortenReturnsShortUrl() {
        UrlCommand urlCommand = new UrlCommand();
        urlCommand.setUrl("https://www.google.com");
        UrlShortenResponse expectedResponse = new UrlShortenResponse("https://www.google.com", "HKl_v");
        when(urlService.shortenUrl(any())).thenReturn(expectedResponse);
        UrlShortenResponse actualResponse = urlController.shorten(urlCommand);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void redirectReturnsSeeOtherStatus() {
        String shortUrl = "HKl_v";
        String originalUrl = "https://www.google.com";
        when(urlService.redirectUrl(any())).thenReturn(originalUrl);
        HttpEntity<Object> responseEntity = urlController.redirect(shortUrl, "1");
        assertEquals(HttpStatus.SEE_OTHER, ((ResponseEntity) responseEntity).getStatusCode());
    }

    @Test
    void redirectReturnsBadRequestForInvalidApiVersion() {
        String shortUrl = "HKl_v";
        String invalidApiVersion = "2";
        HttpEntity<Object> responseEntity = urlController.redirect(shortUrl, invalidApiVersion);
        assertEquals(HttpStatus.BAD_REQUEST, ((ResponseEntity) responseEntity).getStatusCode());
        assertEquals("Invalid API version", responseEntity.getBody());
    }
}
