package com.udla.markenx.api.classroom.users.domain.exceptions;

public class InvalidEmailFormatException extends EmailException {
    public InvalidEmailFormatException() {
        super("El formato del correo electrónico no es válido");
    }
}
