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
    private String urlOriginal;

    @Indexed
    private String urlCorta;

    private Long visitas = 0L;
    public UrlEntity(String urlOriginal, String urlCorta) {
        this.urlOriginal = urlOriginal;
        this.urlCorta = urlCorta;
    }

    public UrlEntity(String id, String urlOriginal, String urlCorta, Long visitas) {
        this.id = id;
        this.urlOriginal = urlOriginal;
        this.urlCorta = urlCorta;
        this.visitas = visitas;
    }

    public UrlEntity(String urlCorta, String urlOriginal, Long visitas) {
        this.urlCorta = urlCorta;
        this.urlOriginal = urlOriginal;
        this.visitas = visitas;
    }

    public UrlEntity() {
    }


}
