package com.udla.markenx.api.game.consumers.domain.exceptions;

public class InvalidConsumerBudgetException extends ConsumerException {
    public InvalidConsumerBudgetException() {
        super("El presupuesto del consumidor no puede ser negativo");
    }
}
