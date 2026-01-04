package com.udla.markenx.api.assignments.domain.exceptions;

public class DueDateNotProvidedException extends AssignmentException {
    public DueDateNotProvidedException() {
        super("La fecha de entrega es obligatoria");
    }
}
