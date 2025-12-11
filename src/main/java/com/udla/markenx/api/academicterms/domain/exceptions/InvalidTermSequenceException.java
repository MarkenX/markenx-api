package com.udla.markenx.api.academicterms.domain.exceptions;

import lombok.Getter;

@Getter
public class InvalidTermSequenceException extends AcademicTermException {

    private final int sequence;

    public InvalidTermSequenceException(int sequence) {
        super(String.format(
                "El número de secuencia %d es inválido. Debe ser un número positivo mayor a cero",
                sequence
        ));
        this.sequence = sequence;
    }
}