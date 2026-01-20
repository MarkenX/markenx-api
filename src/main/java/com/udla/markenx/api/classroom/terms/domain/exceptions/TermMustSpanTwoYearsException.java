package com.udla.markenx.api.classroom.terms.domain.exceptions;

public class TermMustSpanTwoYearsException extends TermException {

    public TermMustSpanTwoYearsException() {
        super("El período académico debe abarcar dos años calendario consecutivos");
    }
}