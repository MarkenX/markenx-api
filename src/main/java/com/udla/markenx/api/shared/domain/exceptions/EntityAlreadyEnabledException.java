package com.udla.markenx.api.shared.domain.exceptions;

import com.udla.markenx.api.shared.domain.models.aggregates.AggregateRoot;

public class EntityAlreadyEnabledException extends AggregateRootException {
    public EntityAlreadyEnabledException(AggregateRoot entity) {
        super(String.format("La entidad %s ya se encuentra habilitada", entity.toString()));
    }
}
