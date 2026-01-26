package com.udla.markenx.api.security.domain.exceptions;

public abstract class UserException extends RuntimeException {
    protected UserException(String message) {
        super(message);
    }

    protected UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
