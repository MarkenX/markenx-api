package com.udla.markenx.api.academicterms.application.commands;

import java.time.LocalDate;

public record SaveAcademicTermCommand(
        LocalDate startDate,
        LocalDate endDate,
        int year,
        boolean isHistorical
) {
}
