package com.udla.markenx.api.students.domain.exceptions;

public class InvalidLastNameException extends PersonalInfoException {
    public InvalidLastNameException() {
        super("El apellido no puede estar vacío ni contener espacios en blanco");
    }
}
