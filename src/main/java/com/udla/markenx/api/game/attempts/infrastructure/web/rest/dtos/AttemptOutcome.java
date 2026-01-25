package com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos;

import com.udla.markenx.api.game.attempts.domain.models.valueobjects.AttemptStatus;
import org.jspecify.annotations.NonNull;

/**
 * REST-layer enum representing the outcome of an attempt.
 * Maps from domain {@link AttemptStatus} for API responses.
 */
public enum AttemptOutcome {

    /**
     * The attempt was successful (score >= minimum required).
     */
    WIN,

    /**
     * The attempt was unsuccessful (score < minimum required).
     */
    LOSE,

    /**
     * The attempt is still in progress (no result yet).
     */
    IN_PROGRESS;

    /**
     * Maps from domain AttemptStatus to REST AttemptOutcome.
     *
     * @param status the domain attempt status
     * @return the corresponding REST outcome
     */
    public static @NonNull AttemptOutcome from(@NonNull AttemptStatus status) {
        return switch (status) {
            case APPROVED -> WIN;
            case DISAPPROVED -> LOSE;
            case UNKNOWN -> IN_PROGRESS;
        };
    }

    /**
     * Maps from domain AttemptStatus to REST AttemptOutcome.
     * For completed attempts only (no IN_PROGRESS allowed).
     *
     * @param status the domain attempt status
     * @return WIN or LOSE
     * @throws IllegalArgumentException if status is UNKNOWN
     */
    public static @NonNull AttemptOutcome fromCompleted(@NonNull AttemptStatus status) {
        return switch (status) {
            case APPROVED -> WIN;
            case DISAPPROVED -> LOSE;
            case UNKNOWN -> throw new IllegalArgumentException(
                    "Cannot map UNKNOWN status to completed outcome");
        };
    }
}
