package com.udla.markenx.api.videogame.attempts.domain.exceptions;

import com.udla.markenx.api.videogame.attempts.domain.models.valueobjects.AttemptStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class InvalidAttemptStatusTransitionException extends AttemptException {

    private final AttemptStatus current;
    private final AttemptStatus target;

    public InvalidAttemptStatusTransitionException(
            AttemptStatus current,
            AttemptStatus target
    ) {
        super(buildMessage(current, target));
        this.current = current;
        this.target = target;
    }

    @Contract(pure = true)
    private static @NonNull String buildMessage(
            @NonNull AttemptStatus current,
            @NonNull AttemptStatus target
    ) {
        return String.format(
                "No se puede cambiar el estado del intento de '%s' a '%s'.",
                current.getLabel(),
                target.getLabel()
        );
    }

    public AttemptStatus current() {
        return current;
    }

    public AttemptStatus target() {
        return target;
    }
}
