package com.udla.markenx.api.assignments.domain.models.valueobjects;

import com.udla.markenx.api.assignments.domain.exceptions.ScoreIsNotANumberException;
import com.udla.markenx.api.assignments.domain.exceptions.ScoreOutOfAllowedRangeException;

public record AssignmentScore(Double value) {
    public AssignmentScore {
        if (Double.isNaN(value)) {
            throw new ScoreIsNotANumberException();
        }
        if (value < 0 || value > 1) {
            throw new ScoreOutOfAllowedRangeException();
        }
    }
}
