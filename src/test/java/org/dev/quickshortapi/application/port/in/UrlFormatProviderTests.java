package org.dev.quickshortapi.application.port.in;

import org.dev.quickshortapi.application.port.in.format.IUrlFormat;
import org.dev.quickshortapi.application.port.in.format.UrlFormatProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class UrlFormatProviderTests {

    private IUrlFormat urlFormatProvider;

    @BeforeEach
    void setup() {
        urlFormatProvider = new UrlFormatProvider();
    }

    @Test
    void getDateFormatedToStringReturnsCorrectFormat() {
        Date date = new Date();
        String formattedDate = urlFormatProvider.getDateFormatedToString(date);
        assertThat(formattedDate).matches("\\w{3}, \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }

    @Test
    void getDateFormatedToStringReturnsEmptyForNullDate() {
        String formattedDate = urlFormatProvider.getDateFormatedToString(null);
        assertThat(formattedDate).isEmpty();
    }
}