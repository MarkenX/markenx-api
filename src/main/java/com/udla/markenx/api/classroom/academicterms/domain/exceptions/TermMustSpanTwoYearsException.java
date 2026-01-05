package com.udla.markenx.api.classroom.academicterms.domain.exceptions;

public class TermMustSpanTwoYearsException extends AcademicTermException {

    public TermMustSpanTwoYearsException() {
        super("El período académico debe abarcar dos años calendario consecutivos");
    }
}