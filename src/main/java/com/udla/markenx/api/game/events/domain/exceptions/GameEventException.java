package com.udla.markenx.api.game.events.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class GameEventException extends RuntimeException {

    private static final String CODE = "GAME_EVENT_EXCEPTION";
    private static final String ENTITY_NAME = "Evento de juego";

    public GameEventException(String message) {
        super(message);
    }

    public GameEventException(String message, Throwable cause) {
        super(message, cause);
    }

    @Contract("_ -> new")
    public static @NonNull EntityNotFoundException notFoundById(String id) {
        return EntityNotFoundException.byId(CODE, ENTITY_NAME, id);
    }

}
