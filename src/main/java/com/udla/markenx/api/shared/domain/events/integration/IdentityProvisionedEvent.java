package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when identity has been successfully provisioned.
 * This event crosses module boundaries (e.g., security -> students).
 *
 * @param sourceEntityId The ID of the entity that requested identity (e.g., id)
 * @param identityId The ID of the created identity (e.g., userId)
 */
public record IdentityProvisionedEvent(
        String sourceEntityId,
        String identityId
) {
}
