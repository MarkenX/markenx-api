package com.udla.markenx.api.classroom.terms.application.ports.out;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;

public interface TermCommandRepository {
    Term save(Term term);
}
