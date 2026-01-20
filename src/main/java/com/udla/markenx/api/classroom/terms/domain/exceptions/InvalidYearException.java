package com.udla.markenx.api.classroom.terms.domain.exceptions;

import lombok.Getter;

@Getter
public class InvalidYearException extends TermException {

    private final int providedYear;
    private final int maxAllowedYear;

    public InvalidYearException(int providedYear, int minAllowedYear, int maxAllowedYear) {
        super(String.format(
                "El año académico proporcionado (%d) está fuera del rango: (%d) - (%d)",
                providedYear,
                minAllowedYear,
                maxAllowedYear
        ));
        this.providedYear = providedYear;
        this.maxAllowedYear = maxAllowedYear;
    }
}