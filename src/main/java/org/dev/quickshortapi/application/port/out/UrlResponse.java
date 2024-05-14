package org.dev.quickshortapi.application.port.out;

import lombok.Data;
import org.dev.quickshortapi.application.port.in.format.IUrlFormat;

import java.util.Date;

@Data
public class UrlResponse {
    private String id;
    private String originalUrl;
    private String shortUrl;
    private Long visits;
    private String createdDate;
    private String lastVisitedDate;

    public UrlResponse(String id, String originalUrl, String shortUrl, Long visits, Date createdDate, Date lastVisitedDate, IUrlFormat urlFormatProviderr) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.visits = visits;
        this.createdDate =urlFormatProviderr.getDateFormatedToString(createdDate);
        this.lastVisitedDate = urlFormatProviderr.getDateFormatedToString(lastVisitedDate);
    }
}