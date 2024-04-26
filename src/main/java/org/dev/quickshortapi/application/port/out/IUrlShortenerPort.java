package org.dev.quickshortapi.application.port.out;

public interface IUrlShortenerPort {
    String generateSHAShortUrl(String urlOriginal);
    String generateRandomShortUrl(String urlOriginal);
}
