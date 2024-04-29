package org.dev.quickshortapi.domain;
public class UrlShortenResponse {
        private String originalUrl;
        private String shortUrl;

        public UrlShortenResponse(String originalUrl, String shortUrl) {
            this.originalUrl = originalUrl;
            this.shortUrl = shortUrl;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public String getShortUrl() {
            return shortUrl;
        }
}
