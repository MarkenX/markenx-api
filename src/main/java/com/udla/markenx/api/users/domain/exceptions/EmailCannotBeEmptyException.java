package com.udla.markenx.api.users.domain.exceptions;

public class EmailCannotBeEmptyException extends EmailException {
    public EmailCannotBeEmptyException() {
        super("El correo electrónico no puede ser nulo ni estar vacío");
    }
}
