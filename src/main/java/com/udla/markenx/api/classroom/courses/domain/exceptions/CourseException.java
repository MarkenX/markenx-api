package com.udla.markenx.api.classroom.courses.domain.exceptions;

public class CourseException extends RuntimeException {
    public CourseException(String message) {
        super(message);
    }

    public CourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
