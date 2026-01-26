package com.udla.markenx.api.security.domain.events;

public record UserCreatedEvent(
        String studentId,
        String userId
) {
}
