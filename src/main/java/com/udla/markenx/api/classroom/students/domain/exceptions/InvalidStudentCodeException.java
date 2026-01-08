package com.udla.markenx.api.classroom.students.domain.exceptions;

public class InvalidStudentCodeException extends StudentException {
    public InvalidStudentCodeException() {
        super("El código no puede ser un número negativo o cero");
    }
}
