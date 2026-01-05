package com.udla.markenx.api.videogame.attempts.domain.exceptions;

public class ApprovalRateOutOfRangeException extends AttemptException {
    public ApprovalRateOutOfRangeException() {
        super("La tasa de aprobación debe estar entre 0.0 y 1.0");
    }
}

