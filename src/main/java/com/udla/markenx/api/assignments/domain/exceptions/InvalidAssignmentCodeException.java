package com.udla.markenx.api.assignments.domain.exceptions;

public class InvalidAssignmentCodeException extends AssignmentException {
    public InvalidAssignmentCodeException(String message) {
        super("El código no puede ser un número negativo o cero");
    }
}
