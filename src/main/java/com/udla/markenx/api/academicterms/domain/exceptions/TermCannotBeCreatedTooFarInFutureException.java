package com.udla.markenx.api.academicterms.domain.exceptions;

import lombok.Getter;

@Getter
public class TermCannotBeCreatedTooFarInFutureException extends AcademicTermException {

    private final int endYear;
    private final int maxAllowedYear;

    public TermCannotBeCreatedTooFarInFutureException(int endYear, int maxAllowedYear) {
        super(String.format(
                "No se puede crear un período académico que finalice en el año %d. " +
                        "Solo se permiten períodos hasta el año %d (máximo un año en el futuro)",
                endYear,
                maxAllowedYear
        ));
        this.endYear = endYear;
        this.maxAllowedYear = maxAllowedYear;
    }
}
