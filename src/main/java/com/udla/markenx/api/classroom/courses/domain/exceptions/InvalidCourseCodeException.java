package com.udla.markenx.api.classroom.courses.domain.exceptions;

public class InvalidCourseCodeException extends CourseException {
    public InvalidCourseCodeException() {
        super("El código no puede ser un número negativo o cero");
    }
}
