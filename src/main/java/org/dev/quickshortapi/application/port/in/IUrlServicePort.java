package org.dev.quickshortapi.application.port.in;

import org.dev.quickshortapi.domain.UrlEstadisticasResponse;

public interface IUrlServicePort {
    String acortarURL(UrlCommand urlCommand);
    String redirigirURL(String urlCorta);
    void deleteUrlbyUrlCorta(String urlCorta);
    UrlEstadisticasResponse estadisticasURL(String urlCorta);
    void deleteCachebyUrlCorta(String urlCorta);
}
