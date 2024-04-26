package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Url")
@Getter
@Setter
public class UrlEntity {
    @Id
    private String id;

    @Indexed(unique = true)
    private String originalUrl;

    @Indexed
    private String shortUrl;

    private Long visits = 0L;
    public UrlEntity(String originalUrl, String shortUrl) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
    }

    public UrlEntity(String id, String originalUrl, String shortUrl, Long visits) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.visits = visits;
    }

    public UrlEntity(String shortUrl, String originalUrl, Long visits) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.visits = visits;
    }

    public UrlEntity() {
    }


}
