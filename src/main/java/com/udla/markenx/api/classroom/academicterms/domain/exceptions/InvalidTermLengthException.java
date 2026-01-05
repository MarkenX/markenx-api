package com.udla.markenx.api.classroom.academicterms.domain.exceptions;

import lombok.Getter;

@Getter
public class InvalidTermLengthException extends AcademicTermException {

    private final long actualMonths;
    private final int minMonths;
    private final int maxMonths;

    public InvalidTermLengthException(long actualMonths, int minMonths, int maxMonths) {
        super(String.format(
                "La duración del período académico (%d meses) es inválida. " +
                        "Debe tener entre %d y %d meses de duración",
                actualMonths,
                minMonths,
                maxMonths
        ));
        this.actualMonths = actualMonths;
        this.minMonths = minMonths;
        this.maxMonths = maxMonths;
    }
}
