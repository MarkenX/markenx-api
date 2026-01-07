package com.udla.markenx.api.game.actions.domain.exceptions;

public class InvalidDimensionReferenceException extends ActionException {
    public InvalidDimensionReferenceException() {
        super("La referencia a la dimensión no puede ser nula o estar en blanco");
    }
}
