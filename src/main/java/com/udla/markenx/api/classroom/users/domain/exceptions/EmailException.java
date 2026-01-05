package com.udla.markenx.api.classroom.users.domain.exceptions;

public abstract class EmailException extends RuntimeException {
    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
