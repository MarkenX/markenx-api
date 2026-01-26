package com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.requests;

import java.time.LocalDate;

public record UpdateTermRequestDTO(
        LocalDate startDate,
        LocalDate endDate,
        int year
) {
}
