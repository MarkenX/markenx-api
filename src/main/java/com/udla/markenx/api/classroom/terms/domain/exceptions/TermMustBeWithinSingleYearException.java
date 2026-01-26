package com.udla.markenx.api.classroom.terms.domain.exceptions;

public class TermMustBeWithinSingleYearException extends TermException {

    public TermMustBeWithinSingleYearException() {
        super("El período académico debe estar contenido dentro de un solo año calendario");
    }
}