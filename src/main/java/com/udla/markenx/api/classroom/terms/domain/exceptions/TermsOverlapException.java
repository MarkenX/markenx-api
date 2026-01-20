package com.udla.markenx.api.classroom.terms.domain.exceptions;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;

public class TermsOverlapException extends TermException {

    public TermsOverlapException(Term a, Term b) {
        super(String.format("Academic term %s overlaps with %s", a.toString(), b.toString()));
    }
}
