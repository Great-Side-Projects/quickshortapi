package org.dev.quickshortapi.domain;

import lombok.Data;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Data
public class Url {
    private String id;
    private String urlOriginal;
    private String urlCorta;
    private Long visitas = 0L;

    public Url(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public Url(String id, String urlOriginal, String urlCorta, Long visitas) {
        this.id = id;
        this.urlOriginal = urlOriginal;
        this.urlCorta = urlCorta;
        this.visitas = visitas;
    }


    public boolean isValidUrl() {
        //Todo: improve URL validation
        try {
            new URL(this.urlOriginal).toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public void incrementVisitas() {
        this.visitas++;
    }
}
