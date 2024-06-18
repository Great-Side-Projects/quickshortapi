package org.dev.quickshortapi.domain.event.cache;

import org.dev.quickshortapi.domain.event.Event;
import org.dev.quickshortapi.domain.event.EventType;
import java.util.Date;

public class UrlFindByIdCacheEvent extends Event<String> {

    public UrlFindByIdCacheEvent(String id, Date date, EventType type, String data) {
        this.setId(id);
        this.setDate(date);
        this.setType(type);
        this.setData(data);
    }

    public UrlFindByIdCacheEvent() {
    }
}
