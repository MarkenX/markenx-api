package com.udla.markenx.api.classroom.terms.application.ports.in.commands;

import java.time.LocalDate;

public record UpdateTermCommand(
        String id,
        LocalDate startDate,
        LocalDate endDate,
        int year
) {
}
