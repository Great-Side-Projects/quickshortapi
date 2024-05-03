package org.dev.quickshortapi.common.shortener;

import org.dev.quickshortapi.common.exceptionhandler.UrlInternalServerErrorException;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Component
public class UrlShortener implements IUrlShortener {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int URL_LENGTH = 8;

    private Random random = new Random();

    @Override
    public String generateSHAShortUrl(String urlOriginal) {
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
            //Define and throw a dedicated exception instead of using a generic one.
            throw new UrlInternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public String generateRandomShortUrl() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < URL_LENGTH; i++) {
            int idx = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(idx));
        }
        return sb.toString();
    }
}
