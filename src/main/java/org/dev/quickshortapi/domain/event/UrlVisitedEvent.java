package org.dev.quickshortapi.domain.event;

import java.util.Date;

public class UrlVisitedEvent extends Event<UrlEvent>{

    public UrlVisitedEvent(String id, Date date, EventType type, UrlEvent data) {
        this.setId(id);
        this.setDate(date);
        this.setType(type);
        this.setData(data);
    }

    public UrlVisitedEvent() {
    }
}
