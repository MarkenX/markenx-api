package com.udla.markenx.api.security.domain.events;

public record UserIdentityRollbackEvent(
        String userId,
        String reason
) {}
