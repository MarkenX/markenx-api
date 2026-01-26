package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when an entity requests identity enabling.
 * This event crosses module boundaries (e.g., students -> security).
 *
 * @param sourceEntityId The ID of the entity requesting enable (e.g., studentId)
 * @param identityId The ID of the identity to enable (e.g., userId)
 * @param email The email of the identity to enable
 */
public record IdentityEnableRequestedEvent(
        String sourceEntityId,
        String identityId,
        String email
) {
}
