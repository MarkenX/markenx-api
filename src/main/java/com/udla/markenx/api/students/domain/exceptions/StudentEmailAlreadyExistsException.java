package com.udla.markenx.api.students.domain.exceptions;

public class StudentEmailAlreadyExistsException extends StudentException {
    public StudentEmailAlreadyExistsException(String email) {
        super("Ya existe un estudiante registrado con el correo electrónico: " + email);
    }
}
