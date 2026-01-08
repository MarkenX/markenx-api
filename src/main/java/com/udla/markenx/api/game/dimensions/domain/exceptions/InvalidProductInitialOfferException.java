package com.udla.markenx.api.game.dimensions.domain.exceptions;

public class InvalidProductInitialOfferException extends DimensionException {
    public InvalidProductInitialOfferException() {
        super("La oferta inicial del producto debe estar en el rango de 0 a 1");
    }
}