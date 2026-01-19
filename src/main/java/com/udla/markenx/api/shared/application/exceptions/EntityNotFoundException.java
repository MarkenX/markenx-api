package com.udla.markenx.api.shared.application.exceptions;

import com.udla.markenx.api.shared.domain.exceptions.EntityException;

@SuppressWarnings("LombokGetterMayBeUsed")
public class EntityNotFoundException extends EntityException {

    private final String entityName;
    private final String entityId;

    public EntityNotFoundException(String entityName, String entityId) {
        super(entityName + " no encontrad@ con id: " + entityId);
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityId() {
        return entityId;
    }
}
