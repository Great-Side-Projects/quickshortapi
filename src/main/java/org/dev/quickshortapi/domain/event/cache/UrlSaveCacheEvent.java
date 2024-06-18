package org.dev.quickshortapi.domain.event.cache;

import org.dev.quickshortapi.domain.event.Event;
import org.dev.quickshortapi.domain.event.EventType;
import java.util.Date;

public class UrlSaveCacheEvent extends Event<UrlEventCache> {

    public UrlSaveCacheEvent(String id, Date date, EventType type, UrlEventCache urlEventCache)
    {
        this.setId(id);
        this.setDate(date);
        this.setType(type);
        this.setData(urlEventCache);
    }

    public UrlSaveCacheEvent() {
    }
}
