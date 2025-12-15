package com.udla.markenx.api.shared.domain.exceptions;

public abstract class EntityException extends RuntimeException {
    public EntityException(String message) {
        super(message);
    }

    public EntityException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
