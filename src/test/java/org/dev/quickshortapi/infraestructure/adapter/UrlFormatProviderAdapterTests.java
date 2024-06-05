package org.dev.quickshortapi.infraestructure.adapter;

import org.dev.quickshortapi.application.port.format.IUrlFormatProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;

class UrlFormatProviderAdapterTests {

    private IUrlFormatProviderPort urlFormatProviderAdapter;

    @BeforeEach
    void setup() {
        urlFormatProviderAdapter = new UrlFormatProviderAdapter();
    }

    @Test
    void getDateFormatedToStringReturnsCorrectFormat() {
        Date date = new Date();
        String formattedDate = urlFormatProviderAdapter.getDateFormatedToString(date);
        assertThat(formattedDate).matches("[\\wáéíóúñ]{1,3}, \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }

    @Test
    void getDateFormatedToStringReturnsEmptyForNullDate() {
        String formattedDate = urlFormatProviderAdapter.getDateFormatedToString(null);
        assertThat(formattedDate).isEmpty();
    }
}