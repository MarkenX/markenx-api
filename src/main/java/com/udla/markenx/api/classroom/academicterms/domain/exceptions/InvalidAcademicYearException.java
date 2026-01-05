package com.udla.markenx.api.classroom.academicterms.domain.exceptions;

import lombok.Getter;

@Getter
public class InvalidAcademicYearException extends AcademicTermException {

    private final int providedYear;
    private final int maxAllowedYear;

    public InvalidAcademicYearException(int providedYear, int minAllowedYear, int maxAllowedYear) {
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