package com.udla.markenx.api.assignments.domain.exceptions;

public class InvalidCurrentAttemptException extends AssignmentException {

    public InvalidCurrentAttemptException() {
        super("El número de intento actual debe ser mayor o igual a cero y no puede superar el máximo de intentos permitidos.");
    }
}
