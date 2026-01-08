package com.udla.markenx.api.game.events.domain.exceptions;

public class InvalidWeightMultiplierException extends GameEventException {
    public InvalidWeightMultiplierException() {
        super("El multiplicador de peso no puede ser negativo");
    }
}
