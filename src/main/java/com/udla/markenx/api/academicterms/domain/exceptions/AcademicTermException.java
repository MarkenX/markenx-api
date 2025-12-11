package com.udla.markenx.api.academicterms.domain.exceptions;

public abstract class AcademicTermException extends RuntimeException {

    protected AcademicTermException(String message) {
        super(message);
    }

    protected AcademicTermException(String message, Throwable cause) {
        super(message, cause);
    }
}