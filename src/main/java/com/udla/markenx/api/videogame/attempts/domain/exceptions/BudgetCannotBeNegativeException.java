package com.udla.markenx.api.videogame.attempts.domain.exceptions;

public class BudgetCannotBeNegativeException extends AttemptException {
    public BudgetCannotBeNegativeException() {
        super("El presupuesto restante no puede ser negativo");
    }
}

