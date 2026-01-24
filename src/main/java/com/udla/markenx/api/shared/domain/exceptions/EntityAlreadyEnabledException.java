package com.udla.markenx.api.shared.domain.exceptions;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;

public class EntityAlreadyEnabledException extends EntityException {
    private static final String CODE = "ENTITY_ALREADY_ENABLED_EXCEPTION";
    public EntityAlreadyEnabledException(Entity entity) {
        super(CODE, String.format("La entidad %s ya se encuentra habilitada", entity.toString()));
    }
}
