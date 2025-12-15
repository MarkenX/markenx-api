package com.udla.markenx.api.shared.domain.exceptions;

import com.udla.markenx.api.shared.domain.models.aggregates.AggregateRoot;

public class EntityAlreadyDisabledException extends AggregateRootException {
    public EntityAlreadyDisabledException(AggregateRoot entity) {
        super(String.format("La entidad %s ya se encuentra deshabilitada", entity.toString()));
    }
}
