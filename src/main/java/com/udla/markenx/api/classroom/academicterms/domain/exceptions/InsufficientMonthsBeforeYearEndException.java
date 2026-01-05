package com.udla.markenx.api.classroom.academicterms.domain.exceptions;

import lombok.Getter;

@Getter
public class InsufficientMonthsBeforeYearEndException extends AcademicTermException {

    private final long actualMonths;
    private final int requiredMonths;

    public InsufficientMonthsBeforeYearEndException(long actualMonths, int requiredMonths) {
        super(String.format(
                "El período académico debe tener al menos %d meses antes del fin del año. " +
                        "Meses actuales hasta fin de año: %d",
                requiredMonths,
                actualMonths
        ));
        this.actualMonths = actualMonths;
        this.requiredMonths = requiredMonths;
    }
}
