package com.udla.markenx.api.classroom.terms.application.ports.in.commands;

import com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.requests.UpdateTermRequestDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

public record UpdateTermCommand(
        String id,
        LocalDate startDate,
        LocalDate endDate,
        int year
) {

    @Contract("_, _ -> new")
    public static @NonNull UpdateTermCommand from(String id, @NonNull UpdateTermRequestDTO request) {
        return new UpdateTermCommand(
                id,
                request.startDate(),
                request.endDate(),
                request.year()
        );
    }
}
