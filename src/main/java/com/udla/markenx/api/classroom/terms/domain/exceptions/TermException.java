package com.udla.markenx.api.classroom.terms.domain.exceptions;

public abstract class TermException extends RuntimeException {

    protected TermException(String message) {
        super(message);
    }

    protected TermException(String message, Throwable cause) {
        super(message, cause);
    }
}