package org.dev.quickshortapi.domain;

import lombok.Getter;

@Getter
public class UrlEstadisticasResponse
{
    private Long visitas;

    public UrlEstadisticasResponse(Long visitas) {
        this.visitas = visitas;
    }
}
