package org.dev.quickshortapi.model;

import lombok.Getter;

@Getter
public class UrlEstadisticasResponse
{
    private Long visitas;
    private Long visitasCache;

    public UrlEstadisticasResponse(Long visitas, Long visitasCache) {
        this.visitas = visitas;
        this.visitasCache = visitasCache;
    }
}
