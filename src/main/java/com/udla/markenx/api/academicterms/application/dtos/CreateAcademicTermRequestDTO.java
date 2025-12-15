package com.udla.markenx.api.academicterms.application.dtos;

import java.time.LocalDate;

public record CreateAcademicTermRequestDTO(
        LocalDate startDate,
        LocalDate endDate,
        int year
) {
}
