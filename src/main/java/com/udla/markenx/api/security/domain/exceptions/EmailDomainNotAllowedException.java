package com.udla.markenx.api.security.domain.exceptions;

public class EmailDomainNotAllowedException extends EmailException {
    public EmailDomainNotAllowedException(String domain) {
        super("El dominio del correo electrónico no está permitido: " + domain);
    }
}
