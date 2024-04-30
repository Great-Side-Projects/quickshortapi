package org.dev.quickshortapi.domain;

import lombok.Getter;
import org.dev.quickshortapi.common.format.IUrlFormat;
import java.util.Date;

@Getter
public class UrlStatisticsResponse
{
    private Long visits;
    private String createdDate;
    private String lastVisitedDate;
    public UrlStatisticsResponse(Long visits, Date createdDate, Date lastVisitedDate, IUrlFormat urlFormatProvider) {
        this.visits = visits;
        this.createdDate = urlFormatProvider.getDateFormatedToString(createdDate);
        this.lastVisitedDate = urlFormatProvider.getDateFormatedToString(lastVisitedDate);
    }
}
