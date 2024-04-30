package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.common.event.Event;

public interface IUrlEventStreamingPort {

    void sendVisitedEvent(Url url);
    void visitedEvent(Event<Url> url);
}
