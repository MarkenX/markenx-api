package com.udla.markenx.api.game.dimensions.domain.exceptions;

public class InvalidDimensionDisplayNameException extends DimensionException {
    public InvalidDimensionDisplayNameException() {
        super("El nombre visible de la dimensión no puede ser nulo o estar en blanco");
    }
}
