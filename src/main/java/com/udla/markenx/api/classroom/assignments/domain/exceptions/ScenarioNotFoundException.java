package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class ScenarioNotFoundException extends AssignmentException {
    public ScenarioNotFoundException(String scenarioId) {
        super("El escenario con ID '" + scenarioId + "' no existe");
    }
}
