package org.dev.quickshortapi.domain;

import lombok.Getter;

@Getter
public class UrlStatisticsResponse
{
    private Long visits;

    public UrlStatisticsResponse(Long visits) {
        this.visits = visits;
    }
}
