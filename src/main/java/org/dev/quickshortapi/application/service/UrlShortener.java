package org.dev.quickshortapi.application.service;

import org.dev.quickshortapi.application.port.out.IUrlShortenerPort;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Component
public class UrlShortener implements IUrlShortenerPort {

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
            //Todo: handle exception properly
            e.printStackTrace();
            throw new RuntimeException(e);
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
}
