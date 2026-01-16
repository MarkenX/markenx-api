package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when identity provisioning has failed.
 * This event crosses module boundaries (e.g., security -> students).
 *
 * @param sourceEntityId The ID of the entity that requested identity (e.g., studentId)
 * @param reason The reason for the failure
 */
public record IdentityProvisioningFailedEvent(
        String sourceEntityId,
        String reason
) {
}
