package com.udla.markenx.api.classroom.terms.application.ports.in.commands;

import com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.requests.UpdateTermStatusRequestDTO;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record ChangeTermStatusCommand(
        String id,
        LifecycleStatus targetStatus
) {

    @Contract("_, _ -> new")
    public static @NonNull ChangeTermStatusCommand from(String id, @NonNull UpdateTermStatusRequestDTO request) {
        return new ChangeTermStatusCommand(
                id,
                request.status()
        );
    }
}
