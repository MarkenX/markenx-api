package com.udla.markenx.api.assignments.domain.exceptions;

public class InvalidAcademicTermIdException extends AssignmentException {
    public InvalidAcademicTermIdException() {
        super("El identificador del periodo académico no puede estar vacío ni contener espacios en blanco");
    }
}
