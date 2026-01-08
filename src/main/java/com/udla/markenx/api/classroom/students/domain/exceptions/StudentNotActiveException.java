package com.udla.markenx.api.classroom.students.domain.exceptions;

public class StudentNotActiveException extends StudentException {

    private static final String MESSAGE = "El estudiante con id '%s' no tiene una identidad activa y no puede ser deshabilitado";

    public StudentNotActiveException(String studentId) {
        super(String.format(MESSAGE, studentId));
    }
}
