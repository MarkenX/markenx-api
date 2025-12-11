package com.udla.markenx.api.shared.domain.exceptions;

public abstract class IdentifierException extends RuntimeException {

    protected IdentifierException(String message) {
        super(message);
    }

    protected IdentifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
