package com.udla.markenx.api.game.attempts.domain.exceptions;

public class InvalidStudentIdException extends AttemptException {
    public InvalidStudentIdException() {
        super("El identificador del estudiante no puede estar vacío ni contener espacios en blanco");
    }
}
