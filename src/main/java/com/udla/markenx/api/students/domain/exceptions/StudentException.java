package com.udla.markenx.api.students.domain.exceptions;

public abstract class StudentException extends RuntimeException {
    public StudentException(String message) {
        super(message);
    }

    public StudentException(String message, Throwable cause) {
        super(message, cause);
    }
}
