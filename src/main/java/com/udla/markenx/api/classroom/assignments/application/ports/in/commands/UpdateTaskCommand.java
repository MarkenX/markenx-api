package com.udla.markenx.api.classroom.assignments.application.ports.in.commands;

import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.requests.UpdateTaskRequestDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

public record UpdateTaskCommand(
        String id,
        String title,
        String summary,
        LocalDateTime deadline,
        int maxAttempts
) {

    @Contract("_, _ -> new")
    public static @NonNull UpdateTaskCommand from(String id, @NonNull UpdateTaskRequestDTO request) {
        return  new UpdateTaskCommand(
                id,
                request.title(),
                request.summary(),
                request.deadline(),
                request.maxAttempts()
        );
    }
}
