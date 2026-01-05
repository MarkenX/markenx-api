package com.udla.markenx.api.game.attempts.domain.exceptions;

public class ProfileScoreOutOfRangeException extends AttemptException {
    public ProfileScoreOutOfRangeException() {
        super("La puntuación del perfil debe estar entre 0.0 y 1.0");
    }
}

