package org.dev.quickshortapi.application.port.out;

import lombok.Data;
import org.dev.quickshortapi.application.port.format.IUrlFormatProviderPort;
import java.util.Date;

@Data
public class UrlResponse {
    private String id;
    private String originalUrl;
    private String shortUrl;
    private Long visits;
    private String createdDate;
    private String lastVisitedDate;

    public UrlResponse(String id, String originalUrl, String shortUrl, Long visits, Date createdDate, Date lastVisitedDate, IUrlFormatProviderPort urlFormatProviderAdapter) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.visits = visits;
        this.createdDate = urlFormatProviderAdapter.getDateFormatedToString(createdDate);
        this.lastVisitedDate = urlFormatProviderAdapter.getDateFormatedToString(lastVisitedDate);
    }
}