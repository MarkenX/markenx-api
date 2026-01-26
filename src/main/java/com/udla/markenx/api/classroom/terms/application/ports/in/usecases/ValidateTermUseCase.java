package com.udla.markenx.api.classroom.terms.application.ports.in.usecases;

import com.udla.markenx.api.classroom.terms.application.ports.in.queries.IsUpcomingTermQuery;

public interface ValidateTermUseCase {
    boolean isUpcoming(IsUpcomingTermQuery query);
}
