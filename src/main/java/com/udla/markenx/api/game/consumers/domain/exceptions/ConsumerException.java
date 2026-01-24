package com.udla.markenx.api.game.consumers.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class ConsumerException extends RuntimeException {

    private static final String CODE = "CONSUMER_EXCEPTION";
    private static final String ENTITY_NAME = "Consumidor";

    public ConsumerException(String message) {
        super(message);
    }

    public ConsumerException(String message, Throwable cause) {
        super(message, cause);
    }

    @Contract("_ -> new")
    public static @NonNull EntityNotFoundException notFoundById(String id) {
        return EntityNotFoundException.byId(CODE, ENTITY_NAME, id);
    }
}
