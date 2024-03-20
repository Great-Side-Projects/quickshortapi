package org.dev.quickshortapi.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface IUrlShortener {
    String generarURLCortaSHA(String urlOriginal);
    String generarURLCortaRandom(String urlOriginal);
    boolean isValidUrl(String url);
}
