package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when identity has been successfully disabled.
 * This event crosses module boundaries (e.g., security -> students).
 *
 * @param sourceEntityId The ID of the entity that requested disable (e.g., studentId)
 */
public record IdentityDisabledEvent(
        String sourceEntityId
) {
}
