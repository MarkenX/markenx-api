package com.udla.markenx.api.game.attempts.domain.exceptions;

public class InvalidSessionDateException extends AttemptException {
    public InvalidSessionDateException() {
        super("Session date cannot be null");
    }
}
