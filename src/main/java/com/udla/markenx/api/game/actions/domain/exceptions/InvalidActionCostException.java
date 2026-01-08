package com.udla.markenx.api.game.actions.domain.exceptions;

public class InvalidActionCostException extends ActionException {
    public InvalidActionCostException() {
        super("El costo de la acción no puede ser negativo");
    }
}
