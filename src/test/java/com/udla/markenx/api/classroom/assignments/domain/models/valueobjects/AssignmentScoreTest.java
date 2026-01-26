package com.udla.markenx.api.classroom.assignments.domain.models.valueobjects;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.ScoreIsNotANumberException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.ScoreOutOfAllowedRangeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AssignmentScore Value Object")
class AssignmentScoreTest {

    @Nested
    @DisplayName("Constructor validation")
    class ConstructorValidation {

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.25, 0.5, 0.75, 1.0})
        @DisplayName("givenValidScore_whenCreatingAssignmentScore_thenSucceeds")
        void givenValidScore_whenCreatingAssignmentScore_thenSucceeds(double score) {
            // When
            AssignmentScore assignmentScore = new AssignmentScore(score);

            // Then
            assertThat(assignmentScore.value()).isEqualTo(score);
        }

        @Test
        @DisplayName("givenNaNScore_whenCreatingAssignmentScore_thenThrowsScoreIsNotANumberException")
        void givenNaNScore_whenCreatingAssignmentScore_thenThrowsScoreIsNotANumberException() {
            // Given
            double score = Double.NaN;

            // When & Then
            assertThatThrownBy(() -> new AssignmentScore(score))
                    .isInstanceOf(ScoreIsNotANumberException.class);
        }

        @Test
        @DisplayName("givenNegativeScore_whenCreatingAssignmentScore_thenThrowsScoreOutOfAllowedRangeException")
        void givenNegativeScore_whenCreatingAssignmentScore_thenThrowsScoreOutOfAllowedRangeException() {
            // Given
            double score = -0.1;

            // When & Then
            assertThatThrownBy(() -> new AssignmentScore(score))
                    .isInstanceOf(ScoreOutOfAllowedRangeException.class);
        }

        @Test
        @DisplayName("givenScoreGreaterThanOne_whenCreatingAssignmentScore_thenThrowsScoreOutOfAllowedRangeException")
        void givenScoreGreaterThanOne_whenCreatingAssignmentScore_thenThrowsScoreOutOfAllowedRangeException() {
            // Given
            double score = 1.1;

            // When & Then
            assertThatThrownBy(() -> new AssignmentScore(score))
                    .isInstanceOf(ScoreOutOfAllowedRangeException.class);
        }
    }

    @Nested
    @DisplayName("Comparison behavior")
    class ComparisonBehavior {

        @Test
        @DisplayName("givenHigherScore_whenComparingWithLowerScore_thenIsGreaterOrEqual")
        void givenHigherScore_whenComparingWithLowerScore_thenIsGreaterOrEqual() {
            // Given
            AssignmentScore higher = new AssignmentScore(0.8);
            AssignmentScore lower = new AssignmentScore(0.5);

            // When
            boolean result = higher.isGreaterOrEqualThan(lower);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("givenLowerScore_whenComparingWithHigherScore_thenIsNotGreaterOrEqual")
        void givenLowerScore_whenComparingWithHigherScore_thenIsNotGreaterOrEqual() {
            // Given
            AssignmentScore lower = new AssignmentScore(0.3);
            AssignmentScore higher = new AssignmentScore(0.7);

            // When
            boolean result = lower.isGreaterOrEqualThan(higher);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("givenEqualScores_whenComparing_thenIsGreaterOrEqual")
        void givenEqualScores_whenComparing_thenIsGreaterOrEqual() {
            // Given
            AssignmentScore score1 = new AssignmentScore(0.6);
            AssignmentScore score2 = new AssignmentScore(0.6);

            // When
            boolean result = score1.isGreaterOrEqualThan(score2);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("givenNullScore_whenComparing_thenThrowsIllegalArgumentException")
        void givenNullScore_whenComparing_thenThrowsIllegalArgumentException() {
            // Given
            AssignmentScore score = new AssignmentScore(0.5);

            // When & Then
            assertThatThrownBy(() -> score.isGreaterOrEqualThan(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("nulo");
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoScoresWithSameValue_whenComparing_thenAreEqual")
        void givenTwoScoresWithSameValue_whenComparing_thenAreEqual() {
            // Given
            AssignmentScore score1 = new AssignmentScore(0.75);
            AssignmentScore score2 = new AssignmentScore(0.75);

            // When & Then
            assertThat(score1).isEqualTo(score2);
            assertThat(score1.hashCode()).isEqualTo(score2.hashCode());
        }

        @Test
        @DisplayName("givenTwoScoresWithDifferentValues_whenComparing_thenAreNotEqual")
        void givenTwoScoresWithDifferentValues_whenComparing_thenAreNotEqual() {
            // Given
            AssignmentScore score1 = new AssignmentScore(0.5);
            AssignmentScore score2 = new AssignmentScore(0.6);

            // When & Then
            assertThat(score1).isNotEqualTo(score2);
        }
    }

    @Nested
    @DisplayName("Boundary values")
    class BoundaryValues {

        @Test
        @DisplayName("givenZeroScore_whenCreating_thenSucceeds")
        void givenZeroScore_whenCreating_thenSucceeds() {
            // Given
            double score = 0.0;

            // When
            AssignmentScore assignmentScore = new AssignmentScore(score);

            // Then
            assertThat(assignmentScore.value()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("givenOneScore_whenCreating_thenSucceeds")
        void givenOneScore_whenCreating_thenSucceeds() {
            // Given
            double score = 1.0;

            // When
            AssignmentScore assignmentScore = new AssignmentScore(score);

            // Then
            assertThat(assignmentScore.value()).isEqualTo(1.0);
        }
    }
}
