package com.udla.markenx.api.academicterms.domain.exceptions;

import lombok.Getter;

@Getter
public class InvalidAcademicYearException extends AcademicTermException {

    private final int providedYear;
    private final int maxAllowedYear;

    public InvalidAcademicYearException(int providedYear, int maxAllowedYear) {
        super(String.format(
                "El año académico proporcionado (%d) excede el límite permitido (%d)",
                providedYear,
                maxAllowedYear
        ));
        this.providedYear = providedYear;
        this.maxAllowedYear = maxAllowedYear;
    }
}