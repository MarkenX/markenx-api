package com.udla.markenx.api.shared.domain.models;

import com.udla.markenx.api.shared.domain.exceptions.IdentifierCannotBeNullException;

import java.util.Objects;

public abstract class Identifier {
    protected final String value;

    protected Identifier(String value) {
        if (value == null || value.isBlank()) {
            throw new IdentifierCannotBeNullException();
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Identifier that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
