package com.udla.markenx.api.game.consumers.domain.exceptions;

public class InvalidConsumerAgeException extends ConsumerException {
    public InvalidConsumerAgeException() {
        super("La edad del consumidor debe estar entre 0 y 150 años");
    }
}
