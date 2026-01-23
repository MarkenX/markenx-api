package com.udla.markenx.api.shared.domain.exceptions;

import org.springframework.http.HttpStatus;

public abstract class EntityException extends RuntimeException {

    private final String code;
    private final HttpStatus status;

    protected EntityException(String code, String message) {
        super(message);
        this.code = code;
        this.status = HttpStatus.valueOf(422);
    }

    protected EntityException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = (status == null) ? HttpStatus.valueOf(422) : status;
    }

    public String code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }
}

