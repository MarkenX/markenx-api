package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class InvalidScenarioIdException extends AssignmentException {
    public InvalidScenarioIdException() {
        super("El identificador del escenario no puede estar vacío ni contener espacios en blanco");
    }
}
