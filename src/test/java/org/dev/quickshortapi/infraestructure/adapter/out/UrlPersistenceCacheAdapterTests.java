package org.dev.quickshortapi.infraestructure.adapter.out;

import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.application.port.out.IUrlRepositoryCache;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntityCache;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlPersistenceCacheAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class UrlPersistenceCacheAdapterTests {

    private UrlPersistenceCacheAdapter urlPersistenceCacheAdapter;
    private IUrlRepositoryCache urlRepositoryCache;
    private IUrlEventTemplatePort<String> urlEventRabbitMQTemplateAdapter;

    @BeforeEach
    void setup() {
        urlRepositoryCache = Mockito.mock(IUrlRepositoryCache.class);
        urlEventRabbitMQTemplateAdapter = Mockito.mock(IUrlEventTemplatePort.class);
        urlPersistenceCacheAdapter = new UrlPersistenceCacheAdapter(urlRepositoryCache, urlEventRabbitMQTemplateAdapter);
    }

    @Test
    void saveReturnsSavedUrl() {
        Url url = new Url("https://www.google.com");
        url.setShortUrl("HKl_v");
        UrlEntityCache urlEntityCache = new UrlEntityCache();
        urlEntityCache.setOriginalUrl("https://www.google.com");
        urlEntityCache.setId("HKl_v");
        Mockito.when(urlRepositoryCache.save(any())).thenReturn(urlEntityCache);
        Url savedUrl = urlPersistenceCacheAdapter.save(url);
        assertThat(savedUrl).isEqualToIgnoringGivenFields(url, "createdDate");
    }

    @Test
    void findByIdReturnsUrlForExistingId() {
        UrlEntityCache urlEntityCache = new UrlEntityCache();
        urlEntityCache.setOriginalUrl("www.google.com");
        urlEntityCache.setId("HKl_v");
        Mockito.when(urlRepositoryCache.findById(anyString()))
                .thenReturn(Optional.of(urlEntityCache));
        Optional<Url> url = urlPersistenceCacheAdapter.findById("existingId");
        assertThat(url).isPresent();
    }

    @Test
    void findByIdReturnsEmptyForNonexistentId() {
        Mockito.when(urlRepositoryCache.findById(anyString())).thenReturn(Optional.empty());
        Optional<Url> url = urlPersistenceCacheAdapter.findById("nonexistentId");
        assertThat(url).isEmpty();
    }

    @Test
    void deleteByIdDeletesExistingUrl() {
        Mockito.doNothing().when(urlRepositoryCache).deleteById(anyString());
        urlPersistenceCacheAdapter.deleteById("existingId");
        Mockito.verify(urlRepositoryCache, Mockito.times(1)).deleteById(anyString());
    }
}
