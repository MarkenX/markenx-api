package com.udla.markenx.api.game.consumers.domain.exceptions;

public class InvalidConsumerNameException extends ConsumerException {
    public InvalidConsumerNameException() {
        super("El nombre del consumidor no puede ser nulo o estar en blanco");
    }
}
