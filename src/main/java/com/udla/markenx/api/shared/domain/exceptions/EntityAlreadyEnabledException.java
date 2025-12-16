package com.udla.markenx.api.shared.domain.exceptions;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;

public class EntityAlreadyEnabledException extends EntityException {
    public EntityAlreadyEnabledException(Entity entity) {
        super(String.format("La entidad %s ya se encuentra habilitada", entity.toString()));
    }
}
