package com.udla.markenx.api.classroom.terms.domain.exceptions;

public class TermActiveNotFoundException extends DateIntervalException {

    public TermActiveNotFoundException() {
        super("No se encontró un período académico activo");
    }
}