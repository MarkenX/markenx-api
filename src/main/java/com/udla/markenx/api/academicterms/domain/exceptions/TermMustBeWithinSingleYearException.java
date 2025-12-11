package com.udla.markenx.api.academicterms.domain.exceptions;

public class TermMustBeWithinSingleYearException extends AcademicTermException {

    public TermMustBeWithinSingleYearException() {
        super("El período académico debe estar contenido dentro de un solo año calendario");
    }
}