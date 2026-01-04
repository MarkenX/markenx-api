package com.udla.markenx.api.attempts.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;

public class Attempt extends Entity {

    private final AttemptId id;

    private Attempt(
            AttemptId id
    ) {
        super();
        this.id = id;
    }
}
