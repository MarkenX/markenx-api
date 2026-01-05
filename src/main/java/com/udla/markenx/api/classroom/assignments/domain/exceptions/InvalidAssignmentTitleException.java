package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class InvalidAssignmentTitleException extends AssignmentException {
    public InvalidAssignmentTitleException() {
        super("El título de la asignación no puede estar vacío ni contener espacios en blanco");
    }
}
