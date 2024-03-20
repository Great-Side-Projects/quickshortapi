package org.dev.quickshortapi.repository.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Urls")
@Getter
@Setter
public class Url {
    @Id
    private String id;

    @Indexed(unique = true)
    private String urlOriginal;

    @Indexed
    private String urlCorta;

    private Long visitas = 0L;
    // Getters y setters
    public Url(String urlOriginal, String urlCorta) {
        this.urlOriginal = urlOriginal;
        this.urlCorta = urlCorta;
    }
}