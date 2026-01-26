package com.udla.markenx.api.security.infrastructure.gametoken;

/**
 * Exception thrown when a game token is invalid, expired, or cannot be refreshed.
 */
public class InvalidGameTokenException extends RuntimeException {

    public InvalidGameTokenException(String message) {
        super(message);
    }

    public InvalidGameTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
