package org.dev.quickshortapi.common.Event;

import lombok.Data;
import lombok.ToString;
import org.dev.quickshortapi.domain.Url;

import java.util.Date;

@Data
public abstract class Event <T> {
    private String id;
    private Date date;
    private EventType type;
    private T data;
}