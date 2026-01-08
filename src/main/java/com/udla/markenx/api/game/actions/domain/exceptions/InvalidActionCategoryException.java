package com.udla.markenx.api.game.actions.domain.exceptions;

public class InvalidActionCategoryException extends ActionException {
    public InvalidActionCategoryException() {
        super("La categoría de la acción no es válida");
    }
}
