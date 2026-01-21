package com.udla.markenx.api.game.dimensions.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class DimensionException extends RuntimeException {

    private static final String ENTITY_NAME = "Dimensión";

    public DimensionException(String message) {
        super(message);
    }

    public DimensionException(String message, Throwable cause) {
        super(message, cause);
    }

    @Contract("_ -> new")
    public static @NonNull EntityNotFoundException notFoundById(String id) {
        return EntityNotFoundException.byId(ENTITY_NAME, id);
    }
}
