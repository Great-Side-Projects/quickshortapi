package org.dev.quickshortapi.domain.event;

import lombok.Data;

import java.util.Date;

@Data
public abstract class Event <T> {
    private String id;
    private Date date;
    private EventType type;
    private T data;

     public String toString() {
        return this.getClass().getName()+"{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", data=" + data +
                '}';
    }
}