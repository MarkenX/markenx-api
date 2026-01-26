package com.udla.markenx.api.classroom.assignments.application.ports.in.commands;

import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.requests.UpdateTaskStatusRequestDTO;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record ChangeTaskStatusCommand(
        String id,
        LifecycleStatus targetStatus
) {

    @Contract("_, _ -> new")
    public static @NonNull ChangeTaskStatusCommand from(String id, @NonNull UpdateTaskStatusRequestDTO request) {
        return  new ChangeTaskStatusCommand(
                id,
                request.status()
        );
    }
}
