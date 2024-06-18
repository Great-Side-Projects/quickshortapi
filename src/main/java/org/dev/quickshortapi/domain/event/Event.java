package org.dev.quickshortapi.domain.event;

import java.util.Date;

public abstract class Event <T> {
    protected String id;
    protected Date date;
    protected EventType type;
    protected T data;

     public String toString() {
        return this.getClass().getName()+"{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", data=" + data +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public T getData() {
        return data;
    }
}