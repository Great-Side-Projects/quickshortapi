package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.common.event.UrlEvent;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.common.event.Event;

public interface IUrlEventStreamingPort {

    void sendVisitedEvent(UrlEvent urlEvent);
    void visitedEvent(Event<UrlEvent> urlEvent);
}
