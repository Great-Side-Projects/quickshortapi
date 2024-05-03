package org.dev.quickshortapi;

import org.dev.quickshortapi.infraestructure.adapter.in.web.UrlController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuickShortApiApplicationTests {

    @Autowired
    private UrlController urlController;
    @Test
    void contextLoads() {
        assertThat(urlController).isNotNull();

    }

}
