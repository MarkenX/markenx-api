package com.udla.markenx.api.shared.domain.exceptions;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import org.jspecify.annotations.NonNull;

public class EntityAlreadyDisabledException extends EntityException {
    private static final String CODE = "ENTITY_ALREADY_DISABLED_EXCEPTION";
    public EntityAlreadyDisabledException(@NonNull Entity entity) {
        super(CODE, String.format("La entidad %s ya se encuentra deshabilitada", entity.toString()));
    }
}
