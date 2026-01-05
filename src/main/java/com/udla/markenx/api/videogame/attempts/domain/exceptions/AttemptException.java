package com.udla.markenx.api.videogame.attempts.domain.exceptions;

public class AttemptException extends RuntimeException {
    public AttemptException(String message) {
        super(message);
    }

    public AttemptException(String message, Throwable cause) {
        super(message, cause);
    }
}
