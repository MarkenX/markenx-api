package com.udla.markenx.api.classroom.academicterms.application.ports.in.commands;

import java.time.LocalDate;

public record CreateTermCommand(
        LocalDate startDate,
        LocalDate endDate,
        int year,
        boolean isHistorical
) {
}
