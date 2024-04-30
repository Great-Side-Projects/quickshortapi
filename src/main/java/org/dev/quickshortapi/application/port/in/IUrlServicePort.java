package org.dev.quickshortapi.application.port.in;

import org.dev.quickshortapi.application.port.out.UrlResponse;
import org.dev.quickshortapi.application.port.out.UrlStatisticsResponse;
import org.dev.quickshortapi.application.port.out.UrlShortenResponse;
import org.springframework.data.domain.Page;

public interface IUrlServicePort {
    UrlShortenResponse shortenUrl(UrlCommand urlCommand);
    String redirectUrl(String urlCorta);
    void deleteUrlbyShortUrl(String urlCorta);
    UrlStatisticsResponse getUrlStatistics(String urlCorta);
    void deleteCachebyShortUrl(String urlCorta);
    Page<UrlResponse> getAllUrls(int page);
}
