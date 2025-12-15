package com.udla.markenx.api.shared.application.exceptions;

import com.udla.markenx.api.shared.domain.exceptions.EntityException;

public class EntityNotFoundException extends EntityException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
