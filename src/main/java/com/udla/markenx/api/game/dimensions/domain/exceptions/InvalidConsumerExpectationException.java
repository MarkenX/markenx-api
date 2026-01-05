package com.udla.markenx.api.game.dimensions.domain.exceptions;

public class InvalidConsumerExpectationException extends DimensionException {
    public InvalidConsumerExpectationException() {
        super("La expectativa del consumidor debe estar en el rango de 0 a 1");
    }
}