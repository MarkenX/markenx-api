package com.udla.markenx.api.attempts.domain.exceptions;

public class BudgetCannotBeNegativeException extends AttemptException {
    public BudgetCannotBeNegativeException() {
        super("El presupuesto restante no puede ser negativo");
    }
}

