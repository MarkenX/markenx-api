package com.udla.markenx.api.attempts.domain.exceptions;

public class InvalidTaskIdException extends AttemptException {
    public InvalidTaskIdException(String message) {
        super("El identificador de la tarea no puede estar vacío ni contener espacios en blanco");
    }
}
