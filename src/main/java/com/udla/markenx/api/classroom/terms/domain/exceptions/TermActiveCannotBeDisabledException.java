package com.udla.markenx.api.classroom.terms.domain.exceptions;

public class TermActiveCannotBeDisabledException extends TermException {
    public TermActiveCannotBeDisabledException() {
        super("Un periodo académico activo no puede ser desactivado:");
    }
}
