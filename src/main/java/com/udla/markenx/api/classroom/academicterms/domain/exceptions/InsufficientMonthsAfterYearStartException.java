package com.udla.markenx.api.classroom.academicterms.domain.exceptions;

import lombok.Getter;

@Getter
public class InsufficientMonthsAfterYearStartException extends AcademicTermException {

    private final long actualMonths;
    private final int requiredMonths;

    public InsufficientMonthsAfterYearStartException(long actualMonths, int requiredMonths) {
        super(String.format(
                "El período académico debe extenderse al menos %d meses después del inicio del año. " +
                        "Meses actuales desde inicio de año: %d",
                requiredMonths,
                actualMonths
        ));
        this.actualMonths = actualMonths;
        this.requiredMonths = requiredMonths;
    }
}
