package com.udla.markenx.api.classroom.students.domain.exceptions;

public class InvalidUserIdException extends StudentException {
    public InvalidUserIdException() {
        super("El identificador del usuario no puede estar vacío ni contener espacios en blanco");
    }
}
