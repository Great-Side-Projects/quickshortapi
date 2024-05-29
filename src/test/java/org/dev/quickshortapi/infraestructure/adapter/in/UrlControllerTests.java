package org.dev.quickshortapi.infraestructure.adapter.in;

import org.dev.quickshortapi.application.port.format.IUrlFormatProviderPort;
import org.dev.quickshortapi.application.port.in.IUrlServicePort;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.port.out.UrlResponse;
import org.dev.quickshortapi.application.port.out.UrlShortenResponse;
import org.dev.quickshortapi.application.port.out.UrlStatisticsResponse;
import org.dev.quickshortapi.application.service.UrlService;
import org.dev.quickshortapi.infraestructure.adapter.UrlFormatProviderAdapter;
import org.dev.quickshortapi.infraestructure.adapter.in.web.UrlController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UrlControllerTests {

    private UrlController urlController;
    private IUrlServicePort urlService;
    private IUrlFormatProviderPort urlFormatProviderAdapter;

    @BeforeEach
    void setup() {
        urlService = Mockito.mock(UrlService.class);
        urlFormatProviderAdapter = new UrlFormatProviderAdapter();
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

    @Test
    void shortenReturnsExpectedShortUrl() {
        UrlCommand urlCommand = new UrlCommand();
        urlCommand.setUrl("https://www.google.com");
        UrlShortenResponse expectedResponse = new UrlShortenResponse("https://www.google.com", "HKl_v");
        when(urlService.shortenUrl(any())).thenReturn(expectedResponse);
        UrlShortenResponse actualResponse = urlController.shorten(urlCommand);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void redirectReturnsExpectedOriginalUrl() {
        String shortUrl = "HKl_v";
        String originalUrl = "https://www.google.com";
        when(urlService.redirectUrl(any())).thenReturn(originalUrl);
        HttpEntity<Object> responseEntity = urlController.redirect(shortUrl, "1");
        assertEquals(HttpStatus.SEE_OTHER, ((ResponseEntity) responseEntity).getStatusCode());
        assertEquals(originalUrl, ((ResponseEntity) responseEntity).getHeaders().getLocation().toString());
    }

    @Test
    void statisticsReturnsExpectedStatistics() {
        String shortUrl = "HKl_v";
        UrlStatisticsResponse expectedResponse = new UrlStatisticsResponse(10L, new Date(),new Date(),urlFormatProviderAdapter);
        when(urlService.getUrlStatistics(any())).thenReturn(expectedResponse);
        UrlStatisticsResponse actualResponse = urlController.statistics(shortUrl);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteUrlReturnsNoContent() {
        String shortUrl = "HKl_v";
        doNothing().when(urlService).deleteUrlbyShortUrl(any());
        urlController.delete(shortUrl);
        verify(urlService, times(1)).deleteUrlbyShortUrl(shortUrl);
    }

    @Test
    void deleteCacheReturnsNoContent() {
        String shortUrl = "HKl_v";
        doNothing().when(urlService).deleteCachebyShortUrl(any());
        urlController.deleteCache(shortUrl);
        verify(urlService, times(1)).deleteCachebyShortUrl(shortUrl);
    }

    @Test
    void getAllUrlsReturnsExpectedUrls() {
        int page = 0;
        int pageSize = 10;
        List<UrlResponse> urlResponses = new ArrayList<>();
        urlResponses.add(new UrlResponse("1", "https://www.google.com", "HKl_v", 10L, new Date(),null,urlFormatProviderAdapter));
        Page<UrlResponse> expectedResponse = new PageImpl<>(urlResponses);
        when(urlService.getAllUrls(anyInt(), anyInt())).thenReturn(expectedResponse);
        ResponseEntity<Page<UrlResponse>> actualResponse = urlController.getAllUrls(page, pageSize);
        assertEquals(expectedResponse, actualResponse.getBody());
    }
}
