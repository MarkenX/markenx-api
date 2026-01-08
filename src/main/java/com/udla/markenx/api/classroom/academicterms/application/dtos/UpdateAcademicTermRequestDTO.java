package com.udla.markenx.api.classroom.academicterms.application.dtos;

import java.time.LocalDate;

public record UpdateAcademicTermRequestDTO(
        LocalDate startDate,
        LocalDate endDate,
        int year
) {
}
