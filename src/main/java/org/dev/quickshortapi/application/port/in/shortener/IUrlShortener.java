package org.dev.quickshortapi.application.port.in.shortener;

public interface IUrlShortener {
    String generateSHAShortUrl(String urlOriginal);
    String generateRandomShortUrl();
}
