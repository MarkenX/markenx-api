package com.udla.markenx.api.classroom.academicterms.domain.exceptions;

public abstract class DateIntervalException extends RuntimeException {

    protected DateIntervalException(String message) {
        super(message);
    }

    protected DateIntervalException(String message, Throwable cause) {
        super(message, cause);
    }
}