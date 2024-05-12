package org.dev.quickshortapi.common.shortener;

import org.dev.quickshortapi.common.exceptionhandler.UrlInternalServerErrorException;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class UrlShortener implements IUrlShortener {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int URL_LENGTH = 5;
    private MessageDigest digest;
    private SecureRandom random = new SecureRandom();

    public UrlShortener() {
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new UrlInternalServerErrorException(e.getMessage());
        }
    }
    @Override
    public String generateSHAShortUrl(String urlOriginal) {
        // Calcular el hash de la URL original
        String encodedURL = URLEncoder.encode(urlOriginal, StandardCharsets.UTF_8); // Codificar la URL original
        byte[] hashBytes = digest.digest(encodedURL.getBytes());
        return Base64.getUrlEncoder().encodeToString(hashBytes).substring(0, URL_LENGTH); // Tomamos los primeros 8 caracteres del hash codificado
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