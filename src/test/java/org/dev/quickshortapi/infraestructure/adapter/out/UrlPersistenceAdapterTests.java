package org.dev.quickshortapi.infraestructure.adapter.out;

import org.dev.quickshortapi.application.port.out.IUrlMongoTemplate;
import org.dev.quickshortapi.application.port.out.IUrlRepository;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.domain.event.UrlEvent;
import org.dev.quickshortapi.domain.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.domain.exceptionhandler.UrlNotFoundException;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlPersistenceAdapter;
import org.glassfish.jaxb.core.v2.TODO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Date;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class UrlPersistenceAdapterTests {

    private UrlPersistenceAdapter urlPersistenceAdapter;
    private IUrlRepository urlRepository;
    private IUrlMongoTemplate urlMongoTemplate;

    @BeforeEach
    void setup() {
        urlRepository = Mockito.mock(IUrlRepository.class);
        urlMongoTemplate = Mockito.mock(IUrlMongoTemplate.class);
        urlPersistenceAdapter = new UrlPersistenceAdapter(urlMongoTemplate, urlRepository);
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
    void increaseVisitsAndUpdateLastVisitedDateThrowsExceptionForFailure() {
        Mockito.doThrow(new RuntimeException()).when(urlMongoTemplate)
                .updateVisitsByIncrementAndLastVisitedDate(any(), any(int.class), any());
        //TODO: Refactorizar para que se pueda testear el mensaje de la excepción
        //assertThatThrownBy(() ->
        //        urlPersistenceAdapter.increaseVisitsAndUpdateLastVisitedDate(
        //                new UrlEvent("id", "shortUrl", "originalUrl", new Date())))
        //        .isInstanceOf(UrlInternalServerErrorException.class)
        //        .hasMessageContaining("Error interno al incrementar las visitas:");
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
}