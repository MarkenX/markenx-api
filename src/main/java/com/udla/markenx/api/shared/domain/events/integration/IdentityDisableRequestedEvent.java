package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when an entity requests identity disabling.
 * This event crosses module boundaries (e.g., students -> security).
 *
 * @param sourceEntityId The ID of the entity requesting disable (e.g., id)
 * @param identityId The ID of the identity to disable (e.g., userId)
 * @param email The email of the identity to disable
 */
public record IdentityDisableRequestedEvent(
        String sourceEntityId,
        String identityId,
        String email
) {
}
