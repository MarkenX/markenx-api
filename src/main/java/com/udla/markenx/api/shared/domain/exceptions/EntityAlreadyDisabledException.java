package com.udla.markenx.api.shared.domain.exceptions;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;

public class EntityAlreadyDisabledException extends EntityException {
    public EntityAlreadyDisabledException(Entity entity) {
        super(String.format("La entidad %s ya se encuentra deshabilitada", entity.toString()));
    }
}
