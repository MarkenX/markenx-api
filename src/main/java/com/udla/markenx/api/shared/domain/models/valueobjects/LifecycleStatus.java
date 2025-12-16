package com.udla.markenx.api.shared.domain.models.valueobjects;

import lombok.Getter;

/**
 * Enum that represents the lifecycle status of an entity.
 * It defines two possible statuses:
 * <p>
 * ACTIVE - Indicates that an entity is enabled or operational.
 * DISABLED - Indicates that an entity is not enabled or is non-operational.
 * <p>
 * Each status is associated with a label providing a human-readable description.
 */
@Getter
public enum LifecycleStatus {
    ACTIVE("Habilitado"),
    DISABLED("Deshabilitado");

    private final String label;

    LifecycleStatus(String label) {
        this.label = label;
    }
}
