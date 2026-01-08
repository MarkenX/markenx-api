package com.udla.markenx.api.game.events.domain.exceptions;

public class InvalidEventDescriptionException extends GameEventException {
    public InvalidEventDescriptionException() {
        super("La descripción del evento no puede ser nula o estar en blanco");
    }
}
