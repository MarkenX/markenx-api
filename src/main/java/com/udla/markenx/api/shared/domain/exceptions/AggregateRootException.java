package com.udla.markenx.api.shared.domain.exceptions;

import com.udla.markenx.api.shared.domain.models.aggregates.AggregateRoot;

public abstract class AggregateRootException extends RuntimeException {
    public AggregateRootException(String message) {
        super(message);
    }

    public AggregateRootException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
