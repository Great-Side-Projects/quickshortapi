package org.dev.quickshortapi.domain.event.cache;

import org.dev.quickshortapi.domain.event.Event;
import org.dev.quickshortapi.domain.event.EventType;

import java.util.Date;

public class UrlDeleteByIdCacheEvent extends Event<String> {

    public UrlDeleteByIdCacheEvent(String id, Date date, EventType type, String data) {
        this.setId(id);
        this.setDate(date);
        this.setType(type);
        this.setData(data);
    }

    public UrlDeleteByIdCacheEvent() {
    }
}
