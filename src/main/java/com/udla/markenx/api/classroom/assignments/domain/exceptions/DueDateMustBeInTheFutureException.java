package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class DueDateMustBeInTheFutureException extends AssignmentException {
    public DueDateMustBeInTheFutureException() {
        super("La fecha de entrega debe ser una fecha futura");
    }
}
