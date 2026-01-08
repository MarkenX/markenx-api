package com.udla.markenx.api.game.scenarios.domain.exceptions;

public class InvalidScenarioDescriptionException extends ScenarioException {
    public InvalidScenarioDescriptionException() {
        super("La descripción del escenario no puede estar vacío ni contener espacios en blanco");
    }
}
