package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.domain.event.UrlEvent;
import org.dev.quickshortapi.domain.event.Event;

public interface IUrlEventStreamingPort {

    void sendVisitedEvent(UrlEvent urlEvent);
    void visitedEvent(Event<UrlEvent> urlEvent);
}
