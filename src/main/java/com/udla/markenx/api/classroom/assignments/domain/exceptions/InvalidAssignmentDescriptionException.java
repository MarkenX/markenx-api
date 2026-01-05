package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class InvalidAssignmentDescriptionException extends AssignmentException {
    public InvalidAssignmentDescriptionException() {
        super("La descripción de la asignación no puede estar vacío ni contener espacios en blanco");
    }
}
