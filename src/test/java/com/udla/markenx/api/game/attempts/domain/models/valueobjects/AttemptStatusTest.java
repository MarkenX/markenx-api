package com.udla.markenx.api.game.attempts.domain.models.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AttemptStatus Enum")
class AttemptStatusTest {

    @Nested
    @DisplayName("UNKNOWN transitions")
    class UnknownTransitions {

        @Test
        @DisplayName("givenUnknownStatus_whenTransitioningToApproved_thenIsAllowed")
        void givenUnknownStatus_whenTransitioningToApproved_thenIsAllowed() {
            // Given
            AttemptStatus current = AttemptStatus.UNKNOWN;

            // When
            boolean canTransition = current.canTransitionTo(AttemptStatus.APPROVED);

            // Then
            assertThat(canTransition).isTrue();
        }

        @Test
        @DisplayName("givenUnknownStatus_whenTransitioningToDisapproved_thenIsAllowed")
        void givenUnknownStatus_whenTransitioningToDisapproved_thenIsAllowed() {
            // Given
            AttemptStatus current = AttemptStatus.UNKNOWN;

            // When
            boolean canTransition = current.canTransitionTo(AttemptStatus.DISAPPROVED);

            // Then
            assertThat(canTransition).isTrue();
        }

        @Test
        @DisplayName("givenUnknownStatus_whenTransitioningToUnknown_thenIsNotAllowed")
        void givenUnknownStatus_whenTransitioningToUnknown_thenIsNotAllowed() {
            // Given
            AttemptStatus current = AttemptStatus.UNKNOWN;

            // When
            boolean canTransition = current.canTransitionTo(AttemptStatus.UNKNOWN);

            // Then
            assertThat(canTransition).isFalse();
        }
    }

    @Nested
    @DisplayName("APPROVED transitions")
    class ApprovedTransitions {

        @Test
        @DisplayName("givenApprovedStatus_whenTransitioningToAnyStatus_thenIsNotAllowed")
        void givenApprovedStatus_whenTransitioningToAnyStatus_thenIsNotAllowed() {
            // Given
            AttemptStatus current = AttemptStatus.APPROVED;

            // When & Then
            assertThat(current.canTransitionTo(AttemptStatus.UNKNOWN)).isFalse();
            assertThat(current.canTransitionTo(AttemptStatus.APPROVED)).isFalse();
            assertThat(current.canTransitionTo(AttemptStatus.DISAPPROVED)).isFalse();
        }
    }

    @Nested
    @DisplayName("DISAPPROVED transitions")
    class DisapprovedTransitions {

        @Test
        @DisplayName("givenDisapprovedStatus_whenTransitioningToAnyStatus_thenIsNotAllowed")
        void givenDisapprovedStatus_whenTransitioningToAnyStatus_thenIsNotAllowed() {
            // Given
            AttemptStatus current = AttemptStatus.DISAPPROVED;

            // When & Then
            assertThat(current.canTransitionTo(AttemptStatus.UNKNOWN)).isFalse();
            assertThat(current.canTransitionTo(AttemptStatus.APPROVED)).isFalse();
            assertThat(current.canTransitionTo(AttemptStatus.DISAPPROVED)).isFalse();
        }
    }

    @Nested
    @DisplayName("Labels")
    class Labels {

        @Test
        @DisplayName("givenApprovedStatus_whenGettingLabel_thenReturnsAprobado")
        void givenApprovedStatus_whenGettingLabel_thenReturnsAprobado() {
            // When
            String label = AttemptStatus.APPROVED.getLabel();

            // Then
            assertThat(label).isEqualTo("Aprobado");
        }

        @Test
        @DisplayName("givenDisapprovedStatus_whenGettingLabel_thenReturnsReprobado")
        void givenDisapprovedStatus_whenGettingLabel_thenReturnsReprobado() {
            // When
            String label = AttemptStatus.DISAPPROVED.getLabel();

            // Then
            assertThat(label).isEqualTo("Reprobado");
        }

        @Test
        @DisplayName("givenUnknownStatus_whenGettingLabel_thenReturnsDesconocido")
        void givenUnknownStatus_whenGettingLabel_thenReturnsDesconocido() {
            // When
            String label = AttemptStatus.UNKNOWN.getLabel();

            // Then
            assertThat(label).isEqualTo("Desconocido");
        }
    }
}
