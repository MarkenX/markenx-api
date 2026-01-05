package com.udla.markenx.api.videogame.attempts.domain.models.valueobjects;

import com.udla.markenx.api.attempts.domain.exceptions.*;
import com.udla.markenx.api.videogame.attempts.domain.exceptions.ApprovalRateOutOfRangeException;
import com.udla.markenx.api.videogame.attempts.domain.exceptions.BudgetCannotBeNegativeException;
import com.udla.markenx.api.videogame.attempts.domain.exceptions.CurrentTurnMustBePositiveException;
import com.udla.markenx.api.videogame.attempts.domain.exceptions.ProfileScoreOutOfRangeException;

import java.math.BigDecimal;

public record AttemptResult(
        int currentTurn,
        BigDecimal budgetRemaining,
        double approvalRate,
        double profileScore
) {
    public AttemptResult {
        validateCurrentTurn(currentTurn);
        validateBudgetRemaining(budgetRemaining);
        validateApprovalRate(approvalRate);
        validateProfileScore(profileScore);
    }

    private void validateCurrentTurn(int currentTurn) {
        if (currentTurn <= 0) {
            throw new CurrentTurnMustBePositiveException();
        }
    }

    private void validateBudgetRemaining(BigDecimal budgetRemaining) {
        if (budgetRemaining.compareTo(BigDecimal.ZERO) < 0) {
            throw new BudgetCannotBeNegativeException();
        }
    }

    private void validateApprovalRate(double approvalRate) {
        if (approvalRate < 0.0 || approvalRate > 1.0) {
            throw new ApprovalRateOutOfRangeException();
        }
    }

    private void validateProfileScore(double profileScore) {
        if (profileScore < 0.0 || profileScore > 1.0) {
            throw new ProfileScoreOutOfRangeException();
        }
    }
}