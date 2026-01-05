package com.udla.markenx.api.classroom.users.domain.exceptions;

public class EmailAlreadyExistsException extends EmailException {
    public EmailAlreadyExistsException(String email) {
        super("Ya existe un usuario registrado con el correo electrónico: " + email);
    }
}
