package com.udla.markenx.api.classroom.academicterms.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;

import java.util.UUID;

public class AcademicTermId extends Identifier {
    protected AcademicTermId(String value) {
        super(value);
    }

    public static AcademicTermId generate() {
        return new AcademicTermId(UUID.randomUUID().toString());
    }
}
