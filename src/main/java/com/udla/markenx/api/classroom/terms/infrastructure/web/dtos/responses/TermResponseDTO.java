package com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.responses;

import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public record TermResponseDTO(
        String id,
        LocalDate startDate,
        LocalDate endDate,
        TermStatusResponseDTO status,
        String label
) {

    public record TermStatusResponseDTO(
            String code,
            String label
    ) {
    }

    @Contract("_ -> new")
    public static @NonNull TermResponseDTO from(@NonNull TermPortDTO term) {
        return new TermResponseDTO(
                term.id(),
                term.startDate(),
                term.endDate(),
                new TermStatusResponseDTO(
                        term.status(),
                        term.statusLabel()
                ),
                term.label()
        );
    }

    public static @NonNull Page<TermResponseDTO> from(@NonNull Page<TermPortDTO> page) {
        return page.map(TermResponseDTO::from);
    }
}
