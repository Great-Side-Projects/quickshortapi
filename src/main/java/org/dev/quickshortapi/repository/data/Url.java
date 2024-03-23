package org.dev.quickshortapi.repository.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Url")
@Getter
@Setter
public class Url  {
    @Id
    private String id;

    @Indexed(unique = true)
    private String urlOriginal;

    @Indexed
    private String urlCorta;

    private Long visitas = 0L;
    public Url(String urlOriginal, String urlCorta) {
        this.urlOriginal = urlOriginal;
        this.urlCorta = urlCorta;
    }

    public Url(String id, String urlOriginal, String urlCorta, Long visitas) {
        this.id = id;
        this.urlOriginal = urlOriginal;
        this.urlCorta = urlCorta;
        this.visitas = visitas;
    }

    public Url() {
    }

    public UrlCache toUrlCache() {
        return new UrlCache(this.urlCorta, this.id, this.urlOriginal, this.visitas);
    }
}
