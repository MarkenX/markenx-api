package com.udla.markenx.api.users.domain.events;

public record UserIdentityRollbackEvent(
        String userId,
        String reason
) {}
