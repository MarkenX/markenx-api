package com.udla.markenx.api.classroom.students.domain.exceptions;

public class InvalidCourseIdException extends StudentException {
    public InvalidCourseIdException() {
        super("El identificador del curso no puede estar vacío ni contener espacios en blanco");
    }
}
