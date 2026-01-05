package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class ScoreOutOfAllowedRangeException extends AssignmentException {

    public ScoreOutOfAllowedRangeException() {
        super("El puntaje mínimo para aprobar debe estar entre 0 y 1.");
    }
}
