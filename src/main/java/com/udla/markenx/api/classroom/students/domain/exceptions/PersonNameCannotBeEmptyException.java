package com.udla.markenx.api.classroom.students.domain.exceptions;

public class PersonNameCannotBeEmptyException extends PersonNameException {
    public PersonNameCannotBeEmptyException() {
        super("El nombre de la persona no puede ser nulo ni estar vacío");
    }
}
