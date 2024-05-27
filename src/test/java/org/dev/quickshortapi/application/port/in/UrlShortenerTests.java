package org.dev.quickshortapi.application.port.in;
import org.dev.quickshortapi.application.port.in.shortener.UrlShortener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UrlShortenerTests {

    private UrlShortener urlShortener;

    @BeforeEach
    void setup() {
        urlShortener = new UrlShortener();
    }

    @Test
    void generateSHAShortUrlReturnsExpectedLength() {
        String shortUrl = urlShortener.generateSHAShortUrl("https://example.com");
        assertThat(shortUrl).hasSize(5);
    }

    @Test
    void generateSHAShortUrlReturnsDifferentResultsForDifferentUrls() {
        String shortUrl1 = urlShortener.generateSHAShortUrl("https://example.com");
        String shortUrl2 = urlShortener.generateSHAShortUrl("https://example.org");
        assertThat(shortUrl1).isNotEqualTo(shortUrl2);
    }

    @Test
    void generateRandomShortUrlReturnsExpectedLength() {
        String shortUrl = urlShortener.generateRandomShortUrl();
        assertThat(shortUrl).hasSize(5);
    }

    @Test
    void generateRandomShortUrlReturnsDifferentResultsForSubsequentCalls() {
        String shortUrl1 = urlShortener.generateRandomShortUrl();
        String shortUrl2 = urlShortener.generateRandomShortUrl();
        assertThat(shortUrl1).isNotEqualTo(shortUrl2);
    }
}