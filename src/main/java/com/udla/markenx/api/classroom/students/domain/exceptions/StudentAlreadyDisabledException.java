package com.udla.markenx.api.classroom.students.domain.exceptions;

public class StudentAlreadyDisabledException extends StudentException {

    private static final String MESSAGE = "El estudiante con id '%s' ya se encuentra deshabilitado";

    public StudentAlreadyDisabledException(String studentId) {
        super(String.format(MESSAGE, studentId));
    }
}
