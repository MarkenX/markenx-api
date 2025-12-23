package com.udla.markenx.api.students.domain.exceptions;

public class InvalidFirstNameException extends PersonalInfoException {
    public InvalidFirstNameException() {
        super("El nombre no puede estar vacío ni contener espacios en blanco");
    }
}
