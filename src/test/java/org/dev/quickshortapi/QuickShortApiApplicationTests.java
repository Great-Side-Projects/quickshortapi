package org.dev.quickshortapi;

import org.dev.quickshortapi.common.shortener.IUrlShortener;
import org.dev.quickshortapi.infraestructure.adapter.in.web.UrlController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuickShortApiApplicationTests {

    @Autowired
    private UrlController urlController;

    @Autowired
    private IUrlShortener urlShortener;
    @Test
    void contextLoads() {
        assertThat(urlController).isNotNull();

    }
    @Test
    void generateRandomShortUrlEveryTime() {
        String shortUrl1 = urlShortener.generateRandomShortUrl();
        String shortUrl2 = urlShortener.generateRandomShortUrl();
        assertThat(shortUrl1).isNotEqualTo(shortUrl2);
    }

}
