package com.udla.markenx.api.game.actions.domain.exceptions;

public class InvalidActionEffectDeltaException extends ActionException {
    public InvalidActionEffectDeltaException() {
        super("El delta del efecto debe estar entre -1.0 y 1.0");
    }
}
