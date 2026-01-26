package com.udla.markenx.api.security.domain.events;

public record UserDisableFailedEvent(
        String studentId,
        String message
) {
}
