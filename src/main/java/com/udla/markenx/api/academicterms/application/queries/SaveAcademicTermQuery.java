package com.udla.markenx.api.academicterms.application.queries;

import java.time.LocalDate;

public record SaveAcademicTermQuery(
        LocalDate startDate,
        LocalDate endDate,
        int year,
        boolean isHistorical
) {
}
