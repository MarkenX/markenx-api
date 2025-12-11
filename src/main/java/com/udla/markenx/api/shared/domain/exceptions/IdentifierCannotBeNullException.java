package com.udla.markenx.api.shared.domain.exceptions;

public class IdentifierCannotBeNullException extends IdentifierException {

    public IdentifierCannotBeNullException() {
        super("La ID no puede ser nulo");
    }
}
