package com.udla.markenx.api.classroom.terms.infrastructure.web.dtos;

import java.time.LocalDate;

public record TermDetailResponseDTO(
        String id,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        String label
) {
}
