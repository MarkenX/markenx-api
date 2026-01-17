package com.udla.markenx.api.classroom.terms.domain.exceptions;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;

public class TermsOverlapException extends AcademicTermException {

    public TermsOverlapException(AcademicTerm a, AcademicTerm b) {
        super(String.format("Academic term %s overlaps with %s", a.toString(), b.toString()));
    }
}
