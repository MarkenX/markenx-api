package com.udla.markenx.api.classroom.assignments.domain.models.valueobjects;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.ScoreIsNotANumberException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.ScoreOutOfAllowedRangeException;

public record AssignmentScore(Double value) {
    public AssignmentScore {
        if (Double.isNaN(value)) {
            throw new ScoreIsNotANumberException();
        }
        if (value < 0 || value > 1) {
            throw new ScoreOutOfAllowedRangeException();
        }
    }

    public boolean isGreaterOrEqualThan(AssignmentScore other) {
        if (other == null) {
            throw new IllegalArgumentException("El score de comparación no puede ser nulo.");
        }
        return this.value >= other.value;
    }

}
