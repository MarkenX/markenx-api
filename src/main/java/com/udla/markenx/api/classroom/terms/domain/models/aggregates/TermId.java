package com.udla.markenx.api.classroom.terms.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;

import java.util.UUID;

public class TermId extends Identifier {
    protected TermId(String value) {
        super(value);
    }

    public static TermId generate() {
        return new TermId(UUID.randomUUID().toString());
    }
}
