package com.udla.markenx.api.game.attempts.domain.exceptions;

public class AttemptNotFoundException extends AttemptException {
    public AttemptNotFoundException(String attemptId) {
        super("Attempt not found with id: " + attemptId);
    }
}
