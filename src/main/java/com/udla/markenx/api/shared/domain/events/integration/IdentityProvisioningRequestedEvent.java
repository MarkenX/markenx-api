package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when an entity requires identity provisioning.
 * This event crosses module boundaries (e.g., students -> security).
 *
 * @param sourceEntityId The ID of the entity requesting identity (e.g., attemptId)
 * @param email The email for identity provisioning
 * @param fullName The full name for identity provisioning
 */
public record IdentityProvisioningRequestedEvent(
        String sourceEntityId,
        String email,
        String fullName
) {
}
