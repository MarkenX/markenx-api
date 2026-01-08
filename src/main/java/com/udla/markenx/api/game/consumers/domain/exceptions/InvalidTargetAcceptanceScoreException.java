package com.udla.markenx.api.game.consumers.domain.exceptions;

public class InvalidTargetAcceptanceScoreException extends ConsumerException {
    public InvalidTargetAcceptanceScoreException() {
        super("El puntaje de aceptación objetivo debe estar entre 0.0 y 1.0");
    }
}
