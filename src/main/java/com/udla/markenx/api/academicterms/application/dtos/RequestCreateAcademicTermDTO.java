package com.udla.markenx.api.academicterms.application.dtos;

import java.time.LocalDate;

public record RequestCreateAcademicTermDTO(
        LocalDate startDate,
        LocalDate endDate,
        int year
) {
}
