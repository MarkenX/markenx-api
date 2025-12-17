package com.udla.markenx.api.shared.domain.exceptions;

public abstract class EntityException extends RuntimeException {
    protected EntityException(String message) {
        super(message);
    }

    protected EntityException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
