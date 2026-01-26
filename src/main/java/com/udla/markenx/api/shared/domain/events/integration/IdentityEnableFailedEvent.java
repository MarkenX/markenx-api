package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when enabling an identity has failed.
 *
 * @param sourceEntityId The ID of the source entity (e.g., studentId)
 * @param reason The reason for the failure
 */
public record IdentityEnableFailedEvent(
        String sourceEntityId,
        String reason
) {
}
