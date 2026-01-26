package com.udla.markenx.api.security.domain.events;

public record UserCreationFailedEvent(
        String studentId,
        String message
) {
}