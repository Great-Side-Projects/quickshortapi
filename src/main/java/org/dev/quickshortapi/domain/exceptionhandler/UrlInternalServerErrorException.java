package org.dev.quickshortapi.domain.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UrlInternalServerErrorException extends RuntimeException {
    public UrlInternalServerErrorException(String message) {
        super(message);
    }
}