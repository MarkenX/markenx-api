package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class InvalidCourseIdException extends AssignmentException {
    public InvalidCourseIdException() {
        super("El identificador del curso no puede estar vacío ni contener espacios en blanco");
    }
}
