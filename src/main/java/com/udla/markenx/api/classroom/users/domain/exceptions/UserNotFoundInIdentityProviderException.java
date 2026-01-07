package com.udla.markenx.api.classroom.users.domain.exceptions;

public class UserNotFoundInIdentityProviderException extends UserException {

    private static final String MESSAGE = "El usuario con email '%s' no fue encontrado en el proveedor de identidad";

    public UserNotFoundInIdentityProviderException(String email) {
        super(String.format(MESSAGE, email));
    }
}
