package com.udla.markenx.api.game.dimensions.domain.exceptions;

public class InvalidDimensionDescriptionException extends DimensionException {
    public InvalidDimensionDescriptionException() {
        super("La descripción de la dimensión no puede ser nula o estar en blanco");
    }
}