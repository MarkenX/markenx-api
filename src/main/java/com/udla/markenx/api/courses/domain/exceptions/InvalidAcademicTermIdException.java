package com.udla.markenx.api.courses.domain.exceptions;

public class InvalidAcademicTermIdException extends CourseException {
    public InvalidAcademicTermIdException() {
        super("El identificador del periodo académico del curso no puede estar vacío ni contener espacios en blanco");
    }
}
