package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@RedisHash("UrlCache")
@Getter
@Setter
public class UrlEntityCache implements Serializable {

    private String id;
    private String idDbUrl;
    private String originalUrl;
    @TimeToLive(unit = TimeUnit.MINUTES)
    private long expirationTime;

    public UrlEntityCache() {
    }
    public UrlEntityCache(String id, String idDbUrl, String originalUrl) {
        this.id = id;
        this.idDbUrl = idDbUrl;
        this.originalUrl = originalUrl;
    }
}