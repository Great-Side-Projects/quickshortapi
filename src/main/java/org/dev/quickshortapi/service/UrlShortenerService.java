package org.dev.quickshortapi.service;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Service
public class UrlShortenerService implements IUrlShortener {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int URL_LENGTH = 8;
    @Override
    public String generarURLCortaSHA(String urlOriginal) {
        try {
            // Crear un objeto MessageDigest con el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Calcular el hash de la URL original
            byte[] hashBytes = digest.digest(urlOriginal.getBytes());
            // Convertir el hash a una cadena hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            // Devolver los primeros 8 caracteres del hash como la URL corta
            return sb.toString().substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            // Manejar la excepción si el algoritmo no está disponible
            e.printStackTrace();
            // En este caso, podrías devolver una URL corta predeterminada o lanzar una excepción
            return "default";
        }
    }

    @Override
    public String generarURLCortaRandom(String urlOriginal) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < URL_LENGTH; i++) {
            int idx = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(idx));
        }
        return sb.toString();
    }

    @Override
    public boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
