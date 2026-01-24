package com.udla.markenx.api.game.attempts.domain.models.valueobjects;

import com.udla.markenx.api.game.attempts.domain.exceptions.ApprovalRateOutOfRangeException;
import com.udla.markenx.api.game.attempts.domain.exceptions.BudgetCannotBeNegativeException;
import com.udla.markenx.api.game.attempts.domain.exceptions.CurrentTurnMustBePositiveException;
import com.udla.markenx.api.game.attempts.domain.exceptions.ProfileScoreOutOfRangeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AttemptResult Value Object")
class AttemptResultTest {

    @Nested
    @DisplayName("Constructor validation")
    class ConstructorValidation {

        @Test
        @DisplayName("givenValidValues_whenCreatingAttemptResult_thenSucceeds")
        void givenValidValues_whenCreatingAttemptResult_thenSucceeds() {
            // Given
            int currentTurn = 5;
            BigDecimal budgetRemaining = new BigDecimal("1000.50");
            double approvalRate = 0.75;
            double profileScore = 0.85;

            // When
            AttemptResult result = new AttemptResult(currentTurn, budgetRemaining, approvalRate, profileScore);

            // Then
            assertThat(result.currentTurn()).isEqualTo(5);
            assertThat(result.budgetRemaining()).isEqualTo(new BigDecimal("1000.50"));
            assertThat(result.approvalRate()).isEqualTo(0.75);
            assertThat(result.profileScore()).isEqualTo(0.85);
        }
    }

    @Nested
    @DisplayName("Current turn validation")
    class CurrentTurnValidation {

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 100})
        @DisplayName("givenPositiveTurn_whenCreating_thenSucceeds")
        void givenPositiveTurn_whenCreating_thenSucceeds(int turn) {
            // When
            AttemptResult result = new AttemptResult(turn, BigDecimal.ZERO, 0.5, 0.5);

            // Then
            assertThat(result.currentTurn()).isEqualTo(turn);
        }

        @Test
        @DisplayName("givenZeroTurn_whenCreating_thenThrowsCurrentTurnMustBePositiveException")
        void givenZeroTurn_whenCreating_thenThrowsCurrentTurnMustBePositiveException() {
            // When & Then
            assertThatThrownBy(() -> new AttemptResult(0, BigDecimal.ZERO, 0.5, 0.5))
                    .isInstanceOf(CurrentTurnMustBePositiveException.class);
        }

        @Test
        @DisplayName("givenNegativeTurn_whenCreating_thenThrowsCurrentTurnMustBePositiveException")
        void givenNegativeTurn_whenCreating_thenThrowsCurrentTurnMustBePositiveException() {
            // When & Then
            assertThatThrownBy(() -> new AttemptResult(-1, BigDecimal.ZERO, 0.5, 0.5))
                    .isInstanceOf(CurrentTurnMustBePositiveException.class);
        }
    }

    @Nested
    @DisplayName("Budget validation")
    class BudgetValidation {

        @Test
        @DisplayName("givenZeroBudget_whenCreating_thenSucceeds")
        void givenZeroBudget_whenCreating_thenSucceeds() {
            // When
            AttemptResult result = new AttemptResult(1, BigDecimal.ZERO, 0.5, 0.5);

            // Then
            assertThat(result.budgetRemaining()).isEqualTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("givenPositiveBudget_whenCreating_thenSucceeds")
        void givenPositiveBudget_whenCreating_thenSucceeds() {
            // Given
            BigDecimal budget = new BigDecimal("5000.00");

            // When
            AttemptResult result = new AttemptResult(1, budget, 0.5, 0.5);

            // Then
            assertThat(result.budgetRemaining()).isEqualTo(budget);
        }

        @Test
        @DisplayName("givenNegativeBudget_whenCreating_thenThrowsBudgetCannotBeNegativeException")
        void givenNegativeBudget_whenCreating_thenThrowsBudgetCannotBeNegativeException() {
            // Given
            BigDecimal negativeBudget = new BigDecimal("-100.00");

            // When & Then
            assertThatThrownBy(() -> new AttemptResult(1, negativeBudget, 0.5, 0.5))
                    .isInstanceOf(BudgetCannotBeNegativeException.class);
        }
    }

    @Nested
    @DisplayName("Approval rate validation")
    class ApprovalRateValidation {

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.25, 0.5, 0.75, 1.0})
        @DisplayName("givenValidApprovalRate_whenCreating_thenSucceeds")
        void givenValidApprovalRate_whenCreating_thenSucceeds(double rate) {
            // When
            AttemptResult result = new AttemptResult(1, BigDecimal.ZERO, rate, 0.5);

            // Then
            assertThat(result.approvalRate()).isEqualTo(rate);
        }

        @Test
        @DisplayName("givenNegativeApprovalRate_whenCreating_thenThrowsApprovalRateOutOfRangeException")
        void givenNegativeApprovalRate_whenCreating_thenThrowsApprovalRateOutOfRangeException() {
            // When & Then
            assertThatThrownBy(() -> new AttemptResult(1, BigDecimal.ZERO, -0.1, 0.5))
                    .isInstanceOf(ApprovalRateOutOfRangeException.class);
        }

        @Test
        @DisplayName("givenApprovalRateGreaterThanOne_whenCreating_thenThrowsApprovalRateOutOfRangeException")
        void givenApprovalRateGreaterThanOne_whenCreating_thenThrowsApprovalRateOutOfRangeException() {
            // When & Then
            assertThatThrownBy(() -> new AttemptResult(1, BigDecimal.ZERO, 1.1, 0.5))
                    .isInstanceOf(ApprovalRateOutOfRangeException.class);
        }
    }

    @Nested
    @DisplayName("Profile score validation")
    class ProfileScoreValidation {

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.25, 0.5, 0.75, 1.0})
        @DisplayName("givenValidProfileScore_whenCreating_thenSucceeds")
        void givenValidProfileScore_whenCreating_thenSucceeds(double score) {
            // When
            AttemptResult result = new AttemptResult(1, BigDecimal.ZERO, 0.5, score);

            // Then
            assertThat(result.profileScore()).isEqualTo(score);
        }

        @Test
        @DisplayName("givenNegativeProfileScore_whenCreating_thenThrowsProfileScoreOutOfRangeException")
        void givenNegativeProfileScore_whenCreating_thenThrowsProfileScoreOutOfRangeException() {
            // When & Then
            assertThatThrownBy(() -> new AttemptResult(1, BigDecimal.ZERO, 0.5, -0.1))
                    .isInstanceOf(ProfileScoreOutOfRangeException.class);
        }

        @Test
        @DisplayName("givenProfileScoreGreaterThanOne_whenCreating_thenThrowsProfileScoreOutOfRangeException")
        void givenProfileScoreGreaterThanOne_whenCreating_thenThrowsProfileScoreOutOfRangeException() {
            // When & Then
            assertThatThrownBy(() -> new AttemptResult(1, BigDecimal.ZERO, 0.5, 1.1))
                    .isInstanceOf(ProfileScoreOutOfRangeException.class);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoResultsWithSameValues_whenComparing_thenAreEqual")
        void givenTwoResultsWithSameValues_whenComparing_thenAreEqual() {
            // Given
            AttemptResult result1 = new AttemptResult(5, new BigDecimal("1000"), 0.8, 0.9);
            AttemptResult result2 = new AttemptResult(5, new BigDecimal("1000"), 0.8, 0.9);

            // When & Then
            assertThat(result1).isEqualTo(result2);
            assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
        }

        @Test
        @DisplayName("givenTwoResultsWithDifferentValues_whenComparing_thenAreNotEqual")
        void givenTwoResultsWithDifferentValues_whenComparing_thenAreNotEqual() {
            // Given
            AttemptResult result1 = new AttemptResult(5, new BigDecimal("1000"), 0.8, 0.9);
            AttemptResult result2 = new AttemptResult(6, new BigDecimal("1000"), 0.8, 0.9);

            // When & Then
            assertThat(result1).isNotEqualTo(result2);
        }
    }
}
