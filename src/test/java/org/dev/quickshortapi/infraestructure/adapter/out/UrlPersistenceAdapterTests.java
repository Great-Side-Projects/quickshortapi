package org.dev.quickshortapi.infraestructure.adapter.out;

import org.dev.quickshortapi.application.port.format.IUrlFormatProviderPort;
import org.dev.quickshortapi.application.port.out.IUrlMongoTemplate;
import org.dev.quickshortapi.application.port.out.IUrlRepository;
import org.dev.quickshortapi.application.port.out.UrlResponse;
import org.dev.quickshortapi.application.port.out.UrlStatisticsResponse;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.domain.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlPersistenceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class UrlPersistenceAdapterTests {

    private UrlPersistenceAdapter urlPersistenceAdapter;
    private IUrlRepository urlRepository;
    private IUrlMongoTemplate urlMongoTemplate;
    private IUrlFormatProviderPort urlFormatProviderAdapter;

    @BeforeEach
    void setup() {
        urlRepository = Mockito.mock(IUrlRepository.class);
        urlMongoTemplate = Mockito.mock(IUrlMongoTemplate.class);
        urlPersistenceAdapter = new UrlPersistenceAdapter(urlMongoTemplate, urlRepository, urlFormatProviderAdapter);
    }

    @Test
    void getShortUrlbyOriginalUrlReturnsShortUrl() {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl("www.google.com");
        urlEntity.setShortUrl("HKl_v");
        Mockito.when(urlRepository.findByOriginalUrl(anyString()))
                .thenReturn(Optional.of(urlEntity));
        String shortUrl = urlPersistenceAdapter.getShortUrlbyOriginalUrl("www.google.com");
        assertThat(shortUrl).isEqualTo("HKl_v");
    }

    @Test
    void getShortUrlbyOriginalUrlReturnsEmptyForNonexistentUrl() {
        Mockito.when(urlRepository.findByOriginalUrl(anyString())).thenReturn(Optional.empty());
        String shortUrl = urlPersistenceAdapter.getShortUrlbyOriginalUrl("nonexistentUrl");
        assertThat(shortUrl).isEmpty();
    }

    @Test
    void saveReturnsSavedUrl() {
        Url url = new Url();
        url.setOriginalUrl("www.google.com");
        url.setShortUrl("HKl_v");
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl("www.google.com");
        urlEntity.setShortUrl("HKl_v");
        Mockito.when(urlRepository.save(any())).thenReturn(urlEntity);
        Url savedUrl = urlPersistenceAdapter.save(url);
        assertThat(savedUrl).isEqualToComparingFieldByField(url);
    }

    @Test
    void getUrlOrThrowByShortUrlReturnsUrl() {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl("www.google.com");
        urlEntity.setShortUrl("HKl_v");
        Mockito.when(urlRepository.findByShortUrl(anyString()))
                .thenReturn(Optional.of(urlEntity));
        Optional<Url> url = urlPersistenceAdapter.getUrlOrThrowByShortUrl("HKl_v");
        assertThat(url).isPresent();
    }

    @Test
    void getUrlOrThrowByShortUrlThrowsExceptionForNonexistentUrl() {
        Mockito.when(urlRepository.findByShortUrl(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> urlPersistenceAdapter.getUrlOrThrowByShortUrl("nonexistentUrl"))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessageContaining("URL corta no encontrada");
    }

    @Test
    void deleteUrlbyShortUrlReturnsTrueForExistingUrl() {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl("www.google.com");
        urlEntity.setShortUrl("HKl_v");
        Mockito.when(urlRepository.findByShortUrl(anyString()))
                .thenReturn(Optional.of(urlEntity));
        boolean isDeleted = urlPersistenceAdapter.deleteUrlbyShortUrl("HKl_v");
        assertThat(isDeleted).isTrue();
    }

    @Test
    void deleteUrlbyShortUrlReturnsFalseForNonexistentUrl() {
        Mockito.when(urlRepository.findByShortUrl(anyString())).thenReturn(Optional.empty());
        boolean isDeleted = urlPersistenceAdapter.deleteUrlbyShortUrl("nonexistentUrl");
        assertThat(isDeleted).isFalse();
    }

    @Test
    void getShortUrlbyOriginalUrlReturnsEmptyWhenUrlNotFound() {
        Mockito.when(urlRepository.findByOriginalUrl(anyString())).thenReturn(Optional.empty());
        String shortUrl = urlPersistenceAdapter.getShortUrlbyOriginalUrl("nonexistentUrl");
        assertThat(shortUrl).isEmpty();
    }

    @Test
    void existCollisionbyShortUrlReturnsTrueWhenUrlExists() {
        Mockito.when(urlRepository.findByShortUrl(anyString())).thenReturn(Optional.of(new UrlEntity()));
        boolean exists = urlPersistenceAdapter.existCollisionbyShortUrl("existingShortUrl");
        assertThat(exists).isTrue();
    }

    @Test
    void existCollisionbyShortUrlReturnsFalseWhenUrlDoesNotExist() {
        Mockito.when(urlRepository.findByShortUrl(anyString())).thenReturn(Optional.empty());
        boolean exists = urlPersistenceAdapter.existCollisionbyShortUrl("nonexistentShortUrl");
        assertThat(exists).isFalse();
    }

    @Test
    void getUrlOrThrowByShortUrlThrowsExceptionWhenUrlNotFound() {
        Mockito.when(urlRepository.findByShortUrl(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> urlPersistenceAdapter.getUrlOrThrowByShortUrl("nonexistentShortUrl"))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessageContaining("URL corta no encontrada");
    }

    @Test
    void deleteUrlbyShortUrlReturnsFalseWhenUrlNotFound() {
        Mockito.when(urlRepository.findByShortUrl(anyString())).thenReturn(Optional.empty());
        boolean isDeleted = urlPersistenceAdapter.deleteUrlbyShortUrl("nonexistentShortUrl");
        assertThat(isDeleted).isFalse();
    }

    @Test
    void getStatisticsByShortUrlReturnsEmptyWhenUrlNotFound() {
        Mockito.when(urlRepository.findByShortUrl(anyString())).thenReturn(Optional.empty());
        Optional<UrlStatisticsResponse> statistics = urlPersistenceAdapter.getStatisticsByShortUrl("nonexistentShortUrl");
        assertThat(statistics).isEmpty();
    }

    @Test
    void getAllUrlsReturnsEmptyPageWhenNoUrlsExist() {
        Mockito.when(urlRepository.findAll((Pageable) any())).thenReturn(Page.empty());
        Page<UrlResponse> urls = urlPersistenceAdapter.getAllUrls(0, 10);
        assertThat(urls).isEmpty();
    }
}