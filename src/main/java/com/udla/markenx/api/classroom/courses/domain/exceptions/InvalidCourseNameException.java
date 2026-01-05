package com.udla.markenx.api.classroom.courses.domain.exceptions;

public class InvalidCourseNameException extends CourseException {
    public InvalidCourseNameException() {
        super("El nombre del curso no puede estar vacío ni contener espacios en blanco");
    }
}
