package org.dev.quickshortapi.application.port.out;

import lombok.Getter;
import org.dev.quickshortapi.application.port.format.IUrlFormatProviderPort;
import java.util.Date;

@Getter
public class UrlStatisticsResponse
{
    private Long visits;
    private String createdDate;
    private String lastVisitedDate;
    public UrlStatisticsResponse(
            Long visits,
            Date createdDate,
            Date lastVisitedDate,
            IUrlFormatProviderPort urlFormatProviderAdapter) {
        this.visits = visits;
        this.createdDate = urlFormatProviderAdapter.getDateFormatedToString(createdDate);
        this.lastVisitedDate = urlFormatProviderAdapter.getDateFormatedToString(lastVisitedDate);
    }
}
