package com.udla.markenx.api.academicterms.application.dtos;

import java.time.LocalDate;

public record ResponseAcademicTermDTO(
        String id,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        String label
) {
}
