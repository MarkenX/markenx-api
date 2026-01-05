package com.udla.markenx.api.classroom.users.domain.exceptions;

public class EmailCannotBeEmptyException extends EmailException {
    public EmailCannotBeEmptyException() {
        super("El correo electrónico no puede ser nulo ni estar vacío");
    }
}
