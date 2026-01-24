package com.udla.markenx.api.game.scenarios.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class ScenarioException extends RuntimeException {

    private static final String CODE = "SCENARIO_EXCEPTION";
    private static final String ENTITY_NAME = "Escenario";

    public ScenarioException(String message) {
        super(message);
    }

    public ScenarioException(String message, Throwable cause) {
        super(message, cause);
    }

    @Contract("_ -> new")
    public static @NonNull EntityNotFoundException notFoundById(String id) {
        return new EntityNotFoundException(CODE, ENTITY_NAME, "id", id);
    }
}
