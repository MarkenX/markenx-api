package com.udla.markenx.api.game.actions.domain.exceptions;

public class CircularPrerequisiteException extends ActionException {
    public CircularPrerequisiteException() {
        super("Se detectó una referencia circular en los prerrequisitos de la acción");
    }
}
