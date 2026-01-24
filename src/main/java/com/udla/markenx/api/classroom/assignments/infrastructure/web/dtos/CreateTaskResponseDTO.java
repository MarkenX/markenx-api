package com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos;

import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record CreateTaskResponseDTO(
        String id,
        String label
) {
    @Contract("_ -> new")
    public static @NonNull CreateTaskResponseDTO from(@NonNull TaskPortDTO domain) {
        return new CreateTaskResponseDTO(domain.id(), domain.label());
    }
}
