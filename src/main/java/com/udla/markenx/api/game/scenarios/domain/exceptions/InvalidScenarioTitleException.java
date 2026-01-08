package com.udla.markenx.api.game.scenarios.domain.exceptions;

public class InvalidScenarioTitleException extends ScenarioException {
    public InvalidScenarioTitleException() {
        super("El título del escenario no puede estar vacío ni contener espacios en blanco");
    }
}
