package org.dev.quickshortapi.repository.data;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Urls")
@Getter
public class Url {
    @Id
    private String id;
    private String urlOriginal;
    private String urlCorta;
    // Getters y setters
    public Url(String urlOriginal, String urlCorta) {
        this.urlOriginal = urlOriginal;
        this.urlCorta = urlCorta;
    }
}
