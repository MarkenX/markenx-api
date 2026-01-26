package com.udla.markenx.api.classroom.terms.domain.exceptions;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;
import org.jspecify.annotations.NonNull;

public class TermsOverlapException extends TermException {

    public TermsOverlapException(@NonNull Term a, @NonNull Term b) {
        super(String.format("El periodo académico %s se superpone con %s", a.toString(), b.toString()));
    }
}
