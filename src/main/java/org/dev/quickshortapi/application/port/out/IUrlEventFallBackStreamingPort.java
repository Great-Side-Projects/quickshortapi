package org.dev.quickshortapi.application.port.out;

import org.dev.quickshortapi.domain.Url;

public interface IUrlEventFallBackStreamingPort {
    void fallbackVisitedEvent(Url url, Throwable t);
}
