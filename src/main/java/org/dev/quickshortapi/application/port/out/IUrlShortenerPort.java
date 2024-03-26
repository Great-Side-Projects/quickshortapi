package org.dev.quickshortapi.application.port.out;

public interface IUrlShortenerPort {
    String generarURLCortaSHA(String urlOriginal);
    String generarURLCortaRandom(String urlOriginal);
}
