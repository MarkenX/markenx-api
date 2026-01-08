package com.udla.markenx.api.game.attempts.domain.exceptions;

public class InvalidTurnNumberException extends AttemptException {
    public InvalidTurnNumberException() {
        super("Turn number must be positive");
    }
}
