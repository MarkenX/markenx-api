package com.udla.markenx.api.academicterms.application.dtos;

import java.time.LocalDate;

public record AcademicTermResponseDTO(
        String id,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        String label
) {
}
