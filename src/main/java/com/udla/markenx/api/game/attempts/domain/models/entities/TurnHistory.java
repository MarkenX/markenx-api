package com.udla.markenx.api.game.attempts.domain.models.entities;

import com.udla.markenx.api.game.attempts.domain.exceptions.InvalidTurnNumberException;
import com.udla.markenx.api.game.attempts.domain.exceptions.BudgetCannotBeNegativeException;
import com.udla.markenx.api.game.attempts.domain.exceptions.ApprovalRateOutOfRangeException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("LombokGetterMayBeUsed")
public class TurnHistory {

    private final TurnHistoryId id;
    private final int turnNumber;
    private final double acceptanceAtEnd;
    private final BigDecimal budgetAtEnd;
    private final String eventOccurredTitle;
    private final List<String> actionsTakenIds;

    // region Constructors

    public TurnHistory(
            TurnHistoryId id,
            int turnNumber,
            double acceptanceAtEnd,
            BigDecimal budgetAtEnd,
            @Nullable String eventOccurredTitle,
            List<String> actionsTakenIds
    ) {
        validateTurnNumber(turnNumber);
        validateAcceptanceAtEnd(acceptanceAtEnd);
        validateBudgetAtEnd(budgetAtEnd);

        this.id = id;
        this.turnNumber = turnNumber;
        this.acceptanceAtEnd = acceptanceAtEnd;
        this.budgetAtEnd = budgetAtEnd;
        this.eventOccurredTitle = eventOccurredTitle;
        this.actionsTakenIds = actionsTakenIds != null
                ? new ArrayList<>(actionsTakenIds)
                : new ArrayList<>();
    }

    public TurnHistory(
            String id,
            int turnNumber,
            double acceptanceAtEnd,
            BigDecimal budgetAtEnd,
            @Nullable String eventOccurredTitle,
            List<String> actionsTakenIds
    ) {
        this(new TurnHistoryId(id), turnNumber, acceptanceAtEnd, budgetAtEnd,
             eventOccurredTitle, actionsTakenIds);
    }

    // endregion

    // region Factories

    public static @NonNull TurnHistory create(
            int turnNumber,
            double acceptanceAtEnd,
            BigDecimal budgetAtEnd,
            @Nullable String eventOccurredTitle,
            List<String> actionsTakenIds
    ) {
        return new TurnHistory(
                TurnHistoryId.generate(),
                turnNumber,
                acceptanceAtEnd,
                budgetAtEnd,
                eventOccurredTitle,
                actionsTakenIds
        );
    }

    // endregion

    // region Getters

    public String getId() {
        return id.value();
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public double getAcceptanceAtEnd() {
        return acceptanceAtEnd;
    }

    public BigDecimal getBudgetAtEnd() {
        return budgetAtEnd;
    }

    public @Nullable String getEventOccurredTitle() {
        return eventOccurredTitle;
    }

    public List<String> getActionsTakenIds() {
        return Collections.unmodifiableList(actionsTakenIds);
    }

    // endregion

    // region Validations

    private void validateTurnNumber(int turnNumber) {
        if (turnNumber <= 0) {
            throw new InvalidTurnNumberException();
        }
    }

    private void validateAcceptanceAtEnd(double acceptanceAtEnd) {
        if (acceptanceAtEnd < 0.0 || acceptanceAtEnd > 1.0) {
            throw new ApprovalRateOutOfRangeException();
        }
    }

    private void validateBudgetAtEnd(BigDecimal budgetAtEnd) {
        if (budgetAtEnd.compareTo(BigDecimal.ZERO) < 0) {
            throw new BudgetCannotBeNegativeException();
        }
    }

    // endregion

    // region Equals & HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TurnHistory that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // endregion
}
