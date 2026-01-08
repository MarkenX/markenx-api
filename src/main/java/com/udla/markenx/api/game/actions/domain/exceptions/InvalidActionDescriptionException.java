package com.udla.markenx.api.game.actions.domain.exceptions;

public class InvalidActionDescriptionException extends ActionException {
    public InvalidActionDescriptionException() {
        super("La descripción de la acción no puede ser nula o estar en blanco");
    }
}
