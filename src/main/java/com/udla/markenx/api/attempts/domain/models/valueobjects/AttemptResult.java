package com.udla.markenx.api.attempts.domain.models.valueobjects;

public record AttemptResult(
        int currentTurn,
        int budgetRemaining,
        double approvalRate,
        double profileScore
) {

}
