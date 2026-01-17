package com.udla.markenx.api.classroom.terms.infrastructure.web.dtos;

import java.time.LocalDate;

public record CreateTermRequestDTO(
        LocalDate startDate,
        LocalDate endDate,
        int year
) {
}
