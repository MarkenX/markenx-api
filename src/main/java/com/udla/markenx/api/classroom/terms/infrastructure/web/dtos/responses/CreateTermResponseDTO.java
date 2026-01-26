package com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.responses;

import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record CreateTermResponseDTO(
        String id,
        String label
) {
    @Contract("_ -> new")
    public static @NonNull CreateTermResponseDTO from(@NonNull TermPortDTO term) {
        return new CreateTermResponseDTO(term.id(), term.label());
    }
}
