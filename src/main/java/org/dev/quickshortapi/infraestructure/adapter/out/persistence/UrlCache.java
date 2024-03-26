package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import lombok.Getter;
import lombok.Setter;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEntity;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("UrlCache")
@Getter
@Setter
public class UrlCache implements Serializable {

    private String id;
    private String idDbUrl;
    private String urlOriginal;

    public UrlCache() {
    }
    public UrlCache(String id, String idDbUrl, String urlOriginal) {
        this.id = id;
        this.idDbUrl = idDbUrl;
        this.urlOriginal = urlOriginal;
    }
}