package com.udla.markenx.api.classroom.students.domain.models.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StudentStatus Enum")
class StudentStatusTest {

    @Nested
    @DisplayName("PENDING_IDENTITY transitions")
    class PendingIdentityTransitions {

        @Test
        @DisplayName("givenPendingIdentityStatus_whenTransitioningToActive_thenIsAllowed")
        void givenPendingIdentityStatus_whenTransitioningToActive_thenIsAllowed() {
            // Given
            StudentStatus current = StudentStatus.PENDING_IDENTITY;

            // When
            boolean canTransition = current.canTransitionTo(StudentStatus.ACTIVE);

            // Then
            assertThat(canTransition).isTrue();
        }

        @Test
        @DisplayName("givenPendingIdentityStatus_whenTransitioningToIdentityCreationFailed_thenIsAllowed")
        void givenPendingIdentityStatus_whenTransitioningToIdentityCreationFailed_thenIsAllowed() {
            // Given
            StudentStatus current = StudentStatus.PENDING_IDENTITY;

            // When
            boolean canTransition = current.canTransitionTo(StudentStatus.IDENTITY_CREATION_FAILED);

            // Then
            assertThat(canTransition).isTrue();
        }

        @Test
        @DisplayName("givenPendingIdentityStatus_whenTransitioningToPendingIdentity_thenIsNotAllowed")
        void givenPendingIdentityStatus_whenTransitioningToPendingIdentity_thenIsNotAllowed() {
            // Given
            StudentStatus current = StudentStatus.PENDING_IDENTITY;

            // When
            boolean canTransition = current.canTransitionTo(StudentStatus.PENDING_IDENTITY);

            // Then
            assertThat(canTransition).isFalse();
        }
    }

    @Nested
    @DisplayName("ACTIVE transitions")
    class ActiveTransitions {

        @Test
        @DisplayName("givenActiveStatus_whenTransitioningToAnyStatus_thenIsNotAllowed")
        void givenActiveStatus_whenTransitioningToAnyStatus_thenIsNotAllowed() {
            // Given
            StudentStatus current = StudentStatus.ACTIVE;

            // When & Then
            assertThat(current.canTransitionTo(StudentStatus.PENDING_IDENTITY)).isFalse();
            assertThat(current.canTransitionTo(StudentStatus.ACTIVE)).isFalse();
            assertThat(current.canTransitionTo(StudentStatus.IDENTITY_CREATION_FAILED)).isFalse();
        }
    }

    @Nested
    @DisplayName("IDENTITY_CREATION_FAILED transitions")
    class IdentityCreationFailedTransitions {

        @Test
        @DisplayName("givenIdentityCreationFailedStatus_whenTransitioningToPendingIdentity_thenIsAllowed")
        void givenIdentityCreationFailedStatus_whenTransitioningToPendingIdentity_thenIsAllowed() {
            // Given
            StudentStatus current = StudentStatus.IDENTITY_CREATION_FAILED;

            // When
            boolean canTransition = current.canTransitionTo(StudentStatus.PENDING_IDENTITY);

            // Then
            assertThat(canTransition).isTrue();
        }

        @Test
        @DisplayName("givenIdentityCreationFailedStatus_whenTransitioningToActive_thenIsNotAllowed")
        void givenIdentityCreationFailedStatus_whenTransitioningToActive_thenIsNotAllowed() {
            // Given
            StudentStatus current = StudentStatus.IDENTITY_CREATION_FAILED;

            // When
            boolean canTransition = current.canTransitionTo(StudentStatus.ACTIVE);

            // Then
            assertThat(canTransition).isFalse();
        }

        @Test
        @DisplayName("givenIdentityCreationFailedStatus_whenTransitioningToSameStatus_thenIsNotAllowed")
        void givenIdentityCreationFailedStatus_whenTransitioningToSameStatus_thenIsNotAllowed() {
            // Given
            StudentStatus current = StudentStatus.IDENTITY_CREATION_FAILED;

            // When
            boolean canTransition = current.canTransitionTo(StudentStatus.IDENTITY_CREATION_FAILED);

            // Then
            assertThat(canTransition).isFalse();
        }
    }
}
