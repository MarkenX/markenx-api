package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when identity disabling has failed.
 * This event crosses module boundaries (e.g., security -> students).
 *
 * @param sourceEntityId The ID of the entity that requested disable (e.g., id)
 * @param reason The reason for the failure
 */
public record IdentityDisableFailedEvent(
        String sourceEntityId,
        String reason
) {
}
