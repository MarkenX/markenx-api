package com.udla.markenx.api.students.domain.exceptions;

public class InvalidPersonNameFormatException extends RuntimeException {
    public InvalidPersonNameFormatException() {
        super("""
                El nombre de la persona debe estar compuesto por exactamente dos palabras\s
                y solo puede contener letras, sin números ni caracteres especiales
               \s""");
    }
}
