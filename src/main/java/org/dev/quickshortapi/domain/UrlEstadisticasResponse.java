package org.dev.quickshortapi.domain;

import lombok.Getter;

@Getter
public class UrlEstadisticasResponse
{
    private Long visits;

    public UrlEstadisticasResponse(Long visits) {
        this.visits = visits;
    }
}
