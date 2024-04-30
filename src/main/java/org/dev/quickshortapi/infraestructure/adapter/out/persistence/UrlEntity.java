package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

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

    private Date createdDate;

    private Date lastVisitedDate;

    public UrlEntity(String originalUrl, String shortUrl, Long visits, Date createdDate) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.visits = visits;
        this.createdDate = createdDate;
    }

    public UrlEntity() {
    }
}
