package com.udla.markenx.api.students.domain.exceptions;

public abstract class PersonalInfoException extends RuntimeException {
    public PersonalInfoException(String message) {
        super(message);
    }

    public PersonalInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
