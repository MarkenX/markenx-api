package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class InvalidMaxAttemptsException extends AssignmentException {

    public InvalidMaxAttemptsException() {
        super("La cantidad máxima de intentos debe ser mayor que cero.");
    }
}
