package org.dev.quickshortapi.application.port.in;

import org.dev.quickshortapi.domain.UrlEstadisticasResponse;

public interface IUrlServicePort {
    String shortenUrl(UrlCommand urlCommand);
    String redirectUrl(String urlCorta);
    void deleteUrlbyShortUrl(String urlCorta);
    UrlEstadisticasResponse getUrlStatistics(String urlCorta);
    void deleteCachebyShortUrl(String urlCorta);
}
