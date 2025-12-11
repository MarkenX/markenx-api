package com.udla.markenx.api.academicterms.domain.model.aggregates.academicTerm;

import com.udla.markenx.api.shared.domain.models.Identifier;

import java.util.UUID;

public class AcademicTermId extends Identifier {
    protected AcademicTermId(String value) {
        super(value);
    }

    public static AcademicTermId generate() {
        return new AcademicTermId(UUID.randomUUID().toString());
    }
}
