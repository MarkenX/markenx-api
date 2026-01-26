package com.udla.markenx.api.classroom.students.application.ports.in.commands;

import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.UpdateStudentStatusRequestDTO;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record ChangeStudentStatusCommand(
        String id,
        LifecycleStatus targetStatus
) {

    @Contract("_, _ -> new")
    public static @NonNull ChangeStudentStatusCommand from(String id, @NonNull UpdateStudentStatusRequestDTO request) {
        return new ChangeStudentStatusCommand(
                id,
                request.status()
        );
    }
}
