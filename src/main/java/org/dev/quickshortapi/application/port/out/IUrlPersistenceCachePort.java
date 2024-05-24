package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.domain.Url;
import java.util.Optional;

public interface IUrlPersistenceCachePort {

    Url save(Url url);
    Optional<Url> findById(String id);
    void deleteById(String id);

}
