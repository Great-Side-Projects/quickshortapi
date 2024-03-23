package org.dev.quickshortapi.repository.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("UrlCache")
@Getter
@Setter
public class UrlCache implements Serializable {

    private String id;
    private String idDbUrl;
    private String urlOriginal;
    private Long visitas = 0L;

    public UrlCache() {
    }
    public UrlCache(String id, String idDbUrl, String urlOriginal, Long visitas) {
        this.id = id;
        this.idDbUrl = idDbUrl;
        this.urlOriginal = urlOriginal;
        this.visitas = visitas;
    }
    public Url totUrl() {
        return new Url(this.idDbUrl, this.urlOriginal ,this.id, this.visitas);
    }
}