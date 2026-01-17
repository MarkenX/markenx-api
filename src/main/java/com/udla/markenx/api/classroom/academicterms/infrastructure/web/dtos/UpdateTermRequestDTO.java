package com.udla.markenx.api.classroom.academicterms.infrastructure.web.dtos;

import java.time.LocalDate;

public record UpdateTermRequestDTO(
        LocalDate startDate,
        LocalDate endDate,
        int year
) {
}
