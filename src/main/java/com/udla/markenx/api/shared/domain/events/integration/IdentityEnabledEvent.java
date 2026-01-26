package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when an identity has been successfully enabled.
 *
 * @param sourceEntityId The ID of the source entity (e.g., studentId)
 */
public record IdentityEnabledEvent(
        String sourceEntityId
) {
}
