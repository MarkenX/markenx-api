package com.udla.markenx.api.academicterms.domain.exceptions;

public class StartDateCannotBeNullException extends DateIntervalException {

    public StartDateCannotBeNullException() {
        super("La fecha de inicio no puede ser nula");
    }
}