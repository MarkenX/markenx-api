package com.udla.markenx.api.game.actions.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class ActionException extends RuntimeException {

    private static final String ENTITY_NAME = "Acción";

    public ActionException(String message) {
        super(message);
    }

    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }

    @Contract("_ -> new")
    public static @NonNull EntityNotFoundException notFoundById(String id) {
        return EntityNotFoundException.byId(ENTITY_NAME, id);
    }
}
