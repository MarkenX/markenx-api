package com.udla.markenx.api.classroom.terms.domain.exceptions;

public class EndDateCannotBeNullException extends DateIntervalException {

    public EndDateCannotBeNullException() {
        super("La fecha de fin no puede ser nula");
    }
}