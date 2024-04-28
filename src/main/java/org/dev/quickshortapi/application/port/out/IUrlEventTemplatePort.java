package org.dev.quickshortapi.application.port.out;

import org.springframework.lang.Nullable;
 public interface IUrlEventTemplatePort<K, V> {

    void send(K key, @Nullable V data);
}
