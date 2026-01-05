package com.udla.markenx.api.classroom.academicterms.application.commands;

import java.time.LocalDate;

public record UpdateAcademicTermCommand(
        String id,
        LocalDate startDate,
        LocalDate endDate,
        int year
) {
}
