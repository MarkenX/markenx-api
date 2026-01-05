package com.udla.markenx.api.attempts.domain.exceptions;

public class NullAttemptResultException extends AttemptException {
    public NullAttemptResultException() {
        super("El resultado del intento no puede ser nulo");
    }
}