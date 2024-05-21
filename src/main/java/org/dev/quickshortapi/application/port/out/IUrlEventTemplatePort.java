package org.dev.quickshortapi.application.port.out;

import org.springframework.lang.Nullable;
 public interface IUrlEventTemplatePort<T> {

    void send(@Nullable T data);
}
