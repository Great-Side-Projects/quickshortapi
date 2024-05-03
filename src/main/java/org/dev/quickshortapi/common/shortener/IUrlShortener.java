package org.dev.quickshortapi.common.shortener;

public interface IUrlShortener {
    String generateSHAShortUrl(String urlOriginal);
    String generateRandomShortUrl();
}
