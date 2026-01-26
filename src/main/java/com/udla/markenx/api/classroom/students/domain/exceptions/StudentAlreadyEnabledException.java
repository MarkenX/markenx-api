package com.udla.markenx.api.classroom.students.domain.exceptions;

public class StudentAlreadyEnabledException extends StudentException {

    private static final String MESSAGE = "El estudiante con id '%s' ya se encuentra habilitado";

    public StudentAlreadyEnabledException(String studentId) {
        super(String.format(MESSAGE, studentId));
    }
}
