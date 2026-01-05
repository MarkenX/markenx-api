package com.udla.markenx.api.videogame.attempts.domain.exceptions;

public class ResultsAlreadyRegisteredException extends AttemptException {
    public ResultsAlreadyRegisteredException() {
        super("Los resultados ya han sido registrados para este intento");
    }
}