package com.udla.markenx.api.game.scenarios.domain.exceptions;

public class ScenarioException extends RuntimeException {
    public ScenarioException(String message) {
        super(message);
    }

    public ScenarioException(String message, Throwable cause) {
        super(message, cause);
    }
}
