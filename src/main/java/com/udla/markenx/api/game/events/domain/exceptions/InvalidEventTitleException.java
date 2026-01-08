package com.udla.markenx.api.game.events.domain.exceptions;

public class InvalidEventTitleException extends GameEventException {
    public InvalidEventTitleException() {
        super("El título del evento no puede ser nulo o estar en blanco");
    }
}
