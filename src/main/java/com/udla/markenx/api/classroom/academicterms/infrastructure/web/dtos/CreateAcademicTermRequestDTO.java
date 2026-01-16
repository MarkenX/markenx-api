package com.udla.markenx.api.classroom.academicterms.infrastructure.web.dtos;

import java.time.LocalDate;

public record CreateAcademicTermRequestDTO(
        LocalDate startDate,
        LocalDate endDate,
        int year
) {
}
