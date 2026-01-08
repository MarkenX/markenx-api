package com.udla.markenx.api.classroom.academicterms.domain.exceptions;

import java.time.LocalDate;

public class InvalidDateIntervalException extends DateIntervalException {

    public InvalidDateIntervalException(LocalDate startDate, LocalDate endDate) {
        super(String.format(
                "El intervalo de fechas es inválido: la fecha de fin (%s) no puede ser anterior a la fecha de inicio (%s)",
                endDate, startDate
        ));
    }

    public InvalidDateIntervalException(String message) {
        super(message);
    }
}