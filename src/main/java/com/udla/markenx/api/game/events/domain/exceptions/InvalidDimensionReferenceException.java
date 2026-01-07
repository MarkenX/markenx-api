package com.udla.markenx.api.game.events.domain.exceptions;

public class InvalidDimensionReferenceException extends GameEventException {
    public InvalidDimensionReferenceException() {
        super("La referencia a la dimensión no puede ser nula o estar en blanco");
    }
}
