package org.dev.quickshortapi.application.service;

import org.dev.quickshortapi.application.port.format.IUrlFormatProviderPort;
import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.port.out.*;
import org.dev.quickshortapi.application.port.shortener.IUrlShortenerPort;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.infraestructure.adapter.UrlFormatProviderAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UrlServiceTests {

    @MockBean
    private IUrlShortenerPort urlShortener;

    @MockBean
    private IUrlPersistenceCachePort urlRepositoryCacheAdapter;

    @MockBean
    private IUrlPersistencePort urlPersistenceAdapter;

    @MockBean
    private IUrlEventStreamingPort urlEventStreamingAdapter;

    private IUrlFormatProviderPort urlFormatProviderAdapter;

    private UrlService urlService;

    @BeforeEach
    void setup() {
        urlPersistenceAdapter = Mockito.mock(IUrlPersistencePort.class);
        urlService = new UrlService(urlPersistenceAdapter, urlShortener, urlRepositoryCacheAdapter, urlEventStreamingAdapter);
        urlFormatProviderAdapter = new UrlFormatProviderAdapter();
    }

    @Test
    void shortenUrlGeneratesNewShortUrl() {
        UrlCommand urlCommand = new UrlCommand();
        urlCommand.setUrl("https://www.google.com");
        when(urlPersistenceAdapter.getShortUrlbyOriginalUrl(any())).thenReturn("");
        when(urlShortener.generateSHAShortUrl(any())).thenReturn("HKl_v");
        UrlShortenResponse response = urlService.shortenUrl(urlCommand);
        assertThat(response.getShortUrl()).isEqualTo("HKl_v");
    }

    @Test
    void shortenUrlReturnsExistingShortUrl() {
        UrlCommand urlCommand = new UrlCommand();
        urlCommand.setUrl("https://www.google.com");
        when(urlPersistenceAdapter.getShortUrlbyOriginalUrl(any())).thenReturn("HKl_v");
        UrlShortenResponse response = urlService.shortenUrl(urlCommand);
        assertThat(response.getShortUrl()).isEqualTo("HKl_v");
    }

    @Test
    void redirectUrlReturnsOriginalUrl() {
        Url url = new Url("https://www.google.com");
        url.setShortUrl("HKl_v");
        when(urlRepositoryCacheAdapter.findById(any())).thenReturn(Optional.of(url));
        String originalUrl = urlService.redirectUrl("HKl_v");
        assertThat(originalUrl).isEqualTo("https://www.google.com");
    }

    @Test
    void shortenUrlReturnsExistingShortUrlWhenUrlAlreadyExists() {
        UrlCommand urlCommand = new UrlCommand();
        urlCommand.setUrl("https://www.google.com");
        when(urlPersistenceAdapter.getShortUrlbyOriginalUrl(any())).thenReturn("HKl_v");
        UrlShortenResponse response = urlService.shortenUrl(urlCommand);
        assertEquals("HKl_v", response.getShortUrl());
    }

    @Test
    void shortenUrlGeneratesNewShortUrlWhenUrlDoesNotExist() {
        UrlCommand urlCommand = new UrlCommand();
        urlCommand.setUrl("https://www.google.com");
        when(urlPersistenceAdapter.getShortUrlbyOriginalUrl(any())).thenReturn("");
        when(urlShortener.generateSHAShortUrl(any())).thenReturn("HKl_v");
        UrlShortenResponse response = urlService.shortenUrl(urlCommand);
        assertEquals("HKl_v", response.getShortUrl());
    }

    @Test
    void redirectUrlReturnsOriginalUrlWhenUrlExistsInCache() {
        Url url = new Url("https://www.google.com");
        url.setShortUrl("HKl_v");
        when(urlRepositoryCacheAdapter.findById(any())).thenReturn(Optional.of(url));
        String originalUrl = urlService.redirectUrl("HKl_v");
        assertEquals("https://www.google.com", originalUrl);
    }

    @Test
    void deleteUrlRemovesUrlFromCacheAndDatabase() {
        when(urlPersistenceAdapter.deleteUrlbyShortUrl(any())).thenReturn(true);
        urlService.deleteUrlbyShortUrl("HKl_v");
        verify(urlRepositoryCacheAdapter, times(1)).deleteById("HKl_v");
    }

    @Test
    void getUrlStatisticsReturnsStatisticsWhenUrlExists() {
        UrlStatisticsResponse expectedResponse = new UrlStatisticsResponse(10L, new Date(),new Date(),urlFormatProviderAdapter);
        when(urlPersistenceAdapter.getStatisticsByShortUrl(any())).thenReturn(Optional.of(expectedResponse));
        UrlStatisticsResponse actualResponse = urlService.getUrlStatistics("HKl_v");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getAllUrlsReturnsUrlsWhenUrlsExist() {
        List<UrlResponse> urlResponses = new ArrayList<>();
        urlResponses.add(new UrlResponse("1", "https://www.google.com", "HKl_v", 10L, new Date(),null,urlFormatProviderAdapter));
        Page<UrlResponse> expectedResponse = new PageImpl<>(urlResponses);
        when(urlPersistenceAdapter.getAllUrls(anyInt(), anyInt())).thenReturn(expectedResponse);
        Page<UrlResponse> actualResponse = urlService.getAllUrls(0, 10);
        assertEquals(expectedResponse, actualResponse);
    }
}