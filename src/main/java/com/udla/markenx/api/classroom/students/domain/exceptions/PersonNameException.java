package com.udla.markenx.api.classroom.students.domain.exceptions;

public abstract class PersonNameException extends RuntimeException {
    public PersonNameException(String message) {
        super(message);
    }

    public PersonNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
