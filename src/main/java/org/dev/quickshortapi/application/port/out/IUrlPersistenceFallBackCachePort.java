package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.domain.Url;
import java.util.Optional;

public interface IUrlPersistenceFallBackCachePort {
    Url fallbackSave(Url url, Throwable t);
    Optional<Url> fallbackFindById(String id, Throwable t);
    void fallbackDeleteById(String id, Throwable t);
}
