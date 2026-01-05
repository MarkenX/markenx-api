package com.udla.markenx.api.game.attempts.domain.exceptions;

public class InvalidTaskIdException extends AttemptException {
    public InvalidTaskIdException() {
        super("El identificador de la tarea no puede estar vacío ni contener espacios en blanco");
    }
}
