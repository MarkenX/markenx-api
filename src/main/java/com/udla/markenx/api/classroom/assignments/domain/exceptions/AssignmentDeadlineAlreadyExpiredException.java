package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class AssignmentDeadlineAlreadyExpiredException extends AssignmentException {

    public AssignmentDeadlineAlreadyExpiredException() {
        super("No se puede modificar la tarea porque su fecha límite ya ha expirado.");
    }
}
