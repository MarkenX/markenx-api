package com.udla.markenx.api.assignments.domain.exceptions;

public class DueDateMustBeInTheFutureException extends AssignmentException {
    public DueDateMustBeInTheFutureException() {
        super("La fecha de entrega debe ser una fecha futura");
    }
}
