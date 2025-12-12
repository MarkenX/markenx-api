package com.udla.markenx.api.academicterms.application.dtos;

import java.time.LocalDate;

public record AcademicTermDTO(
        String id,
        int year,
        int sequence,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {
}
