package com.udla.markenx.api.game.dimensions.domain.exceptions;

public class DimensionException extends RuntimeException {
    public DimensionException(String message) {
        super(message);
    }

    public DimensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
