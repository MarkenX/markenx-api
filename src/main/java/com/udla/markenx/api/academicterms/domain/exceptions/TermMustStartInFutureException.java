package com.udla.markenx.api.academicterms.domain.exceptions;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class TermMustStartInFutureException extends AcademicTermException {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final LocalDate startDate;

    public TermMustStartInFutureException(LocalDate startDate) {
        super(String.format(
                "El período académico debe iniciar en una fecha futura. Fecha de inicio proporcionada: %s",
                startDate.format(FORMATTER)
        ));
        this.startDate = startDate;
    }
}
