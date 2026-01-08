package com.udla.markenx.api.game.dimensions.domain.exceptions;

public class InvalidDimensionNameException extends DimensionException {
    public InvalidDimensionNameException() {
        super("El nombre de la dimensión no puede ser nulo o estar en blanco");
    }
}