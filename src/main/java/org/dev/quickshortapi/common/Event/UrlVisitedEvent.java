package org.dev.quickshortapi.common.Event;

import org.dev.quickshortapi.domain.Url;
import java.util.Date;

public class UrlVisitedEvent extends Event<Url>{

    public UrlVisitedEvent(String id, Date date, EventType type, Url data) {
        this.setId(id);
        this.setDate(date);
        this.setType(type);
        this.setData(data);
    }

    public UrlVisitedEvent() {
    }
}
