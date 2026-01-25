package com.udla.markenx.api.classroom.terms.application.ports.in.commands;

import com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.requests.CreateTermRequestDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

public record CreateTermCommand(
        LocalDate startDate,
        LocalDate endDate,
        int year,
        boolean isHistorical
) {

    @Contract("_ -> new")
    public static @NonNull CreateTermCommand from(@NonNull CreateTermRequestDTO request) {
        return new CreateTermCommand(
                request.startDate(),
                request.endDate(),
                request.year(),
                false);
    }
}
