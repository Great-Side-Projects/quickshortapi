package org.dev.quickshortapi.application.service;

import org.dev.quickshortapi.application.port.in.UrlCommand;
import org.dev.quickshortapi.application.port.shortener.IUrlShortenerPort;
import org.dev.quickshortapi.application.port.out.IUrlEventStreamingPort;
import org.dev.quickshortapi.application.port.out.IUrlPersistenceCachePort;
import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
import org.dev.quickshortapi.application.port.out.UrlShortenResponse;
import org.dev.quickshortapi.domain.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
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

    private UrlService urlService;

    @BeforeEach
    void setup() {
        urlPersistenceAdapter = Mockito.mock(IUrlPersistencePort.class);
        urlService = new UrlService(urlPersistenceAdapter, urlShortener, urlRepositoryCacheAdapter, urlEventStreamingAdapter);
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
        Url url = new Url();
        url.setOriginalUrl("https://www.google.com");
        url.setShortUrl("HKl_v");
        when(urlRepositoryCacheAdapter.findById(any())).thenReturn(Optional.of(url));
        String originalUrl = urlService.redirectUrl("HKl_v");
        assertThat(originalUrl).isEqualTo("https://www.google.com");
    }

    @Test
    void deleteUrlRemovesUrlFromCacheAndDatabase() {
        when(urlPersistenceAdapter.deleteUrlbyShortUrl(any())).thenReturn(true);
        urlService.deleteUrlbyShortUrl("HKl_v");
        verify(urlRepositoryCacheAdapter, times(1)).deleteById("HKl_v");
    }
}