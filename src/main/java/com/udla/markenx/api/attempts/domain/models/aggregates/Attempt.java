package com.udla.markenx.api.attempts.domain.models.aggregates;

import com.udla.markenx.api.attempts.domain.models.valueobjects.AttemptResult;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;

public class Attempt extends Entity {

    private final AttemptId id;
    private final AttemptResult result;

    private Attempt(
            AttemptId id,
            AttemptResult result
    ) {
        super();
        this.id = id;
        this.result = result;
    }
}
