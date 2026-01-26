package com.udla.markenx.api.security.domain.events;

public record UserDisabledEvent(
        String studentId,
        String userId
) {
}
