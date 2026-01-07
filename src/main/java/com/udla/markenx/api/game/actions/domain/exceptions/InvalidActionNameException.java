package com.udla.markenx.api.game.actions.domain.exceptions;

public class InvalidActionNameException extends ActionException {
    public InvalidActionNameException() {
        super("El nombre de la acción no puede ser nulo o estar en blanco");
    }
}
