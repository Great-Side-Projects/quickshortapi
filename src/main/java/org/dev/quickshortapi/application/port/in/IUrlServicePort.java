package org.dev.quickshortapi.application.port.in;

import org.dev.quickshortapi.domain.UrlStatisticsResponse;
import org.dev.quickshortapi.domain.UrlShortenResponse;

public interface IUrlServicePort {
    UrlShortenResponse shortenUrl(UrlCommand urlCommand);
    String redirectUrl(String urlCorta);
    void deleteUrlbyShortUrl(String urlCorta);
    UrlStatisticsResponse getUrlStatistics(String urlCorta);
    void deleteCachebyShortUrl(String urlCorta);
}
