package com.udla.markenx.api.assignments.domain.exceptions;

import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class InvalidAssignmentStatusTransitionException extends AssignmentException {

    private final AssignmentStatus current;
    private final AssignmentStatus target;

    public InvalidAssignmentStatusTransitionException(
            AssignmentStatus current,
            AssignmentStatus target
    ) {
        super(buildMessage(current, target));
        this.current = current;
        this.target = target;
    }

    @Contract(pure = true)
    private static @NonNull String buildMessage(
            @NonNull AssignmentStatus current,
            @NonNull AssignmentStatus target
    ) {
        return String.format(
                "No se puede cambiar el estado de la asignación de '%s' a '%s'.",
                current.getLabel(),
                target.getLabel()
        );
    }

    public AssignmentStatus current() {
        return current;
    }

    public AssignmentStatus target() {
        return target;
    }
}
