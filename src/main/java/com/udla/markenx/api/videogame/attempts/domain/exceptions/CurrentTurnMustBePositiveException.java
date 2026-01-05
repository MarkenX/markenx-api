package com.udla.markenx.api.videogame.attempts.domain.exceptions;

public class CurrentTurnMustBePositiveException extends AttemptException {
    public CurrentTurnMustBePositiveException() {
        super("El número de turno debe ser un valor positivo");
    }
}