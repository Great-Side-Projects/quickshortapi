package org.dev.quickshortapi.application.port.shortener;

public interface IUrlShortenerPort {
    String generateSHAShortUrl(String urlOriginal);
    String generateRandomShortUrl();
}
