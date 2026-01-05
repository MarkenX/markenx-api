package com.udla.markenx.api.videogame.attempts.domain.exceptions;

public class NullAttemptResultException extends AttemptException {
    public NullAttemptResultException() {
        super("El resultado del intento no puede ser nulo");
    }
}