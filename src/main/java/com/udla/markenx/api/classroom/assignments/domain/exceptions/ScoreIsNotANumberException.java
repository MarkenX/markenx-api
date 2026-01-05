package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class ScoreIsNotANumberException extends AssignmentException {

    public ScoreIsNotANumberException() {
        super("El puntaje mínimo para aprobar no es un número válido.");
    }
}
