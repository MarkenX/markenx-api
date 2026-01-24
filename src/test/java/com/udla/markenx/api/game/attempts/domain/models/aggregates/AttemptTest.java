package com.udla.markenx.api.game.attempts.domain.models.aggregates;

import com.udla.markenx.api.game.attempts.domain.exceptions.*;
import com.udla.markenx.api.game.attempts.domain.models.valueobjects.AttemptResult;
import com.udla.markenx.api.game.attempts.domain.models.valueobjects.AttemptStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Attempt Aggregate")
class AttemptTest {

    private static final String VALID_TASK_ID = "task-123";
    private static final String VALID_STUDENT_ID = "student-456";

    @Nested
    @DisplayName("Factory method create")
    class FactoryMethodCreate {

        @Test
        @DisplayName("givenValidData_whenCreatingAttempt_thenSucceeds")
        void givenValidData_whenCreatingAttempt_thenSucceeds() {
            // When
            Attempt attempt = Attempt.create(VALID_TASK_ID, VALID_STUDENT_ID);

            // Then
            assertThat(attempt.getId()).isNotNull();
            assertThat(attempt.getTaskId()).isEqualTo(VALID_TASK_ID);
            assertThat(attempt.getStudentId()).isEqualTo(VALID_STUDENT_ID);
            assertThat(attempt.getResult()).isNull();
            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.UNKNOWN);
            assertThat(attempt.getSessionDate()).isNotNull();
            assertThat(attempt.getTurnHistories()).isEmpty();
        }

        @Test
        @DisplayName("givenNullTaskId_whenCreatingAttempt_thenThrowsInvalidTaskIdException")
        void givenNullTaskId_whenCreatingAttempt_thenThrowsInvalidTaskIdException() {
            // When & Then
            assertThatThrownBy(() -> Attempt.create(null, VALID_STUDENT_ID))
                    .isInstanceOf(InvalidTaskIdException.class);
        }

        @Test
        @DisplayName("givenBlankTaskId_whenCreatingAttempt_thenThrowsInvalidTaskIdException")
        void givenBlankTaskId_whenCreatingAttempt_thenThrowsInvalidTaskIdException() {
            // When & Then
            assertThatThrownBy(() -> Attempt.create("   ", VALID_STUDENT_ID))
                    .isInstanceOf(InvalidTaskIdException.class);
        }

        @Test
        @DisplayName("givenNullStudentId_whenCreatingAttempt_thenThrowsInvalidStudentIdException")
        void givenNullStudentId_whenCreatingAttempt_thenThrowsInvalidStudentIdException() {
            // When & Then
            assertThatThrownBy(() -> Attempt.create(VALID_TASK_ID, null))
                    .isInstanceOf(InvalidStudentIdException.class);
        }

        @Test
        @DisplayName("givenBlankStudentId_whenCreatingAttempt_thenThrowsInvalidStudentIdException")
        void givenBlankStudentId_whenCreatingAttempt_thenThrowsInvalidStudentIdException() {
            // When & Then
            assertThatThrownBy(() -> Attempt.create(VALID_TASK_ID, "   "))
                    .isInstanceOf(InvalidStudentIdException.class);
        }
    }

    @Nested
    @DisplayName("Factory method createWithResults")
    class FactoryMethodCreateWithResults {

        @Test
        @DisplayName("givenPassingScore_whenCreatingWithResults_thenStatusIsApproved")
        void givenPassingScore_whenCreatingWithResults_thenStatusIsApproved() {
            // Given
            double finalAcceptance = 0.8;
            double minScoreToPass = 0.7;

            // When
            Attempt attempt = Attempt.createWithResults(
                    VALID_TASK_ID,
                    VALID_STUDENT_ID,
                    LocalDateTime.now(),
                    finalAcceptance,
                    new BigDecimal("1000"),
                    5,
                    0.9,
                    Collections.emptyList(),
                    minScoreToPass
            );

            // Then
            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.APPROVED);
            assertThat(attempt.getResult().approvalRate()).isEqualTo(finalAcceptance);
        }

        @Test
        @DisplayName("givenFailingScore_whenCreatingWithResults_thenStatusIsDisapproved")
        void givenFailingScore_whenCreatingWithResults_thenStatusIsDisapproved() {
            // Given
            double finalAcceptance = 0.5;
            double minScoreToPass = 0.7;

            // When
            Attempt attempt = Attempt.createWithResults(
                    VALID_TASK_ID,
                    VALID_STUDENT_ID,
                    LocalDateTime.now(),
                    finalAcceptance,
                    new BigDecimal("500"),
                    10,
                    0.6,
                    Collections.emptyList(),
                    minScoreToPass
            );

            // Then
            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.DISAPPROVED);
        }

        @Test
        @DisplayName("givenExactMinScore_whenCreatingWithResults_thenStatusIsApproved")
        void givenExactMinScore_whenCreatingWithResults_thenStatusIsApproved() {
            // Given
            double finalAcceptance = 0.7;
            double minScoreToPass = 0.7;

            // When
            Attempt attempt = Attempt.createWithResults(
                    VALID_TASK_ID,
                    VALID_STUDENT_ID,
                    LocalDateTime.now(),
                    finalAcceptance,
                    new BigDecimal("750"),
                    8,
                    0.75,
                    Collections.emptyList(),
                    minScoreToPass
            );

            // Then
            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.APPROVED);
        }
    }

    @Nested
    @DisplayName("Register results")
    class RegisterResults {

        @Test
        @DisplayName("givenUnknownStatusAttempt_whenRegisteringPassingResults_thenTransitionsToApproved")
        void givenUnknownStatusAttempt_whenRegisteringPassingResults_thenTransitionsToApproved() {
            // Given
            Attempt attempt = Attempt.create(VALID_TASK_ID, VALID_STUDENT_ID);
            AttemptResult result = new AttemptResult(5, new BigDecimal("1000"), 0.8, 0.9);
            double minScoreToPass = 0.7;

            // When
            attempt.registerResults(result, minScoreToPass);

            // Then
            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.APPROVED);
            assertThat(attempt.getResult()).isEqualTo(result);
        }

        @Test
        @DisplayName("givenUnknownStatusAttempt_whenRegisteringFailingResults_thenTransitionsToDisapproved")
        void givenUnknownStatusAttempt_whenRegisteringFailingResults_thenTransitionsToDisapproved() {
            // Given
            Attempt attempt = Attempt.create(VALID_TASK_ID, VALID_STUDENT_ID);
            AttemptResult result = new AttemptResult(10, new BigDecimal("200"), 0.5, 0.6);
            double minScoreToPass = 0.7;

            // When
            attempt.registerResults(result, minScoreToPass);

            // Then
            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.DISAPPROVED);
        }

        @Test
        @DisplayName("givenUnknownStatusAttempt_whenRegisteringExactMinScore_thenTransitionsToApproved")
        void givenUnknownStatusAttempt_whenRegisteringExactMinScore_thenTransitionsToApproved() {
            // Given
            Attempt attempt = Attempt.create(VALID_TASK_ID, VALID_STUDENT_ID);
            AttemptResult result = new AttemptResult(7, new BigDecimal("500"), 0.7, 0.75);
            double minScoreToPass = 0.7;

            // When
            attempt.registerResults(result, minScoreToPass);

            // Then
            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.APPROVED);
        }

        @Test
        @DisplayName("givenAttemptWithResults_whenRegisteringResultsAgain_thenThrowsResultsAlreadyRegisteredException")
        void givenAttemptWithResults_whenRegisteringResultsAgain_thenThrowsResultsAlreadyRegisteredException() {
            // Given
            Attempt attempt = Attempt.create(VALID_TASK_ID, VALID_STUDENT_ID);
            AttemptResult result = new AttemptResult(5, new BigDecimal("1000"), 0.8, 0.9);
            attempt.registerResults(result, 0.7);

            AttemptResult newResult = new AttemptResult(6, new BigDecimal("900"), 0.85, 0.95);

            // When & Then
            assertThatThrownBy(() -> attempt.registerResults(newResult, 0.7))
                    .isInstanceOf(ResultsAlreadyRegisteredException.class);
        }

        @Test
        @DisplayName("givenUnknownStatusAttempt_whenRegisteringNullResult_thenThrowsNullAttemptResultException")
        void givenUnknownStatusAttempt_whenRegisteringNullResult_thenThrowsNullAttemptResultException() {
            // Given
            Attempt attempt = Attempt.create(VALID_TASK_ID, VALID_STUDENT_ID);

            // When & Then
            assertThatThrownBy(() -> attempt.registerResults(null, 0.7))
                    .isInstanceOf(NullAttemptResultException.class);
        }
    }

    @Nested
    @DisplayName("Status transitions")
    class StatusTransitions {

        @Test
        @DisplayName("givenApprovedAttempt_whenTransitioningToAnyStatus_thenThrowsInvalidAttemptStatusTransitionException")
        void givenApprovedAttempt_whenTransitioningToAnyStatus_thenThrowsInvalidAttemptStatusTransitionException() {
            // Given
            Attempt attempt = Attempt.createWithResults(
                    VALID_TASK_ID, VALID_STUDENT_ID, LocalDateTime.now(),
                    0.8, new BigDecimal("1000"), 5, 0.9,
                    Collections.emptyList(), 0.7
            );

            // Then verify it's approved and can't transition
            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.APPROVED);
            assertThat(attempt.getStatus().canTransitionTo(AttemptStatus.DISAPPROVED)).isFalse();
        }

        @Test
        @DisplayName("givenDisapprovedAttempt_whenTransitioningToAnyStatus_thenTransitionNotAllowed")
        void givenDisapprovedAttempt_whenTransitioningToAnyStatus_thenTransitionNotAllowed() {
            // Given
            Attempt attempt = Attempt.createWithResults(
                    VALID_TASK_ID, VALID_STUDENT_ID, LocalDateTime.now(),
                    0.5, new BigDecimal("200"), 10, 0.6,
                    Collections.emptyList(), 0.7
            );

            // Then verify it's disapproved and can't transition
            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.DISAPPROVED);
            assertThat(attempt.getStatus().canTransitionTo(AttemptStatus.APPROVED)).isFalse();
        }
    }

    @Nested
    @DisplayName("Turn histories immutability")
    class TurnHistoriesImmutability {

        @Test
        @DisplayName("givenAttempt_whenGettingTurnHistories_thenReturnsUnmodifiableList")
        void givenAttempt_whenGettingTurnHistories_thenReturnsUnmodifiableList() {
            // Given
            Attempt attempt = Attempt.create(VALID_TASK_ID, VALID_STUDENT_ID);

            // When & Then
            assertThatThrownBy(() -> attempt.getTurnHistories().add(null))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Reconstruction from persistence")
    class ReconstructionFromPersistence {

        @Test
        @DisplayName("givenPersistedData_whenReconstructingAttempt_thenAllFieldsAreSet")
        void givenPersistedData_whenReconstructingAttempt_thenAllFieldsAreSet() {
            // Given
            String id = "attempt-id-123";
            int currentTurn = 8;
            BigDecimal budgetRemaining = new BigDecimal("750.50");
            double approvalRate = 0.85;
            double profileScore = 0.92;
            AttemptStatus status = AttemptStatus.APPROVED;
            String taskId = "task-abc";
            String studentId = "student-xyz";
            LocalDateTime sessionDate = LocalDateTime.of(2025, 5, 15, 14, 30);

            // When
            Attempt attempt = new Attempt(
                    id, currentTurn, budgetRemaining, approvalRate, profileScore,
                    status, taskId, studentId, sessionDate, Collections.emptyList()
            );

            // Then
            assertThat(attempt.getId()).isEqualTo(id);
            assertThat(attempt.getResult().currentTurn()).isEqualTo(currentTurn);
            assertThat(attempt.getResult().budgetRemaining()).isEqualTo(budgetRemaining);
            assertThat(attempt.getResult().approvalRate()).isEqualTo(approvalRate);
            assertThat(attempt.getResult().profileScore()).isEqualTo(profileScore);
            assertThat(attempt.getStatus()).isEqualTo(status);
            assertThat(attempt.getTaskId()).isEqualTo(taskId);
            assertThat(attempt.getStudentId()).isEqualTo(studentId);
            assertThat(attempt.getSessionDate()).isEqualTo(sessionDate);
        }

        @Test
        @DisplayName("givenNullSessionDate_whenReconstructing_thenThrowsInvalidSessionDateException")
        void givenNullSessionDate_whenReconstructing_thenThrowsInvalidSessionDateException() {
            // When & Then
            assertThatThrownBy(() -> new Attempt(
                    "id", 5, BigDecimal.ZERO, 0.8, 0.9,
                    AttemptStatus.APPROVED, "task", "student", null, null
            )).isInstanceOf(InvalidSessionDateException.class);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoAttemptsWithSameId_whenComparing_thenAreEqual")
        void givenTwoAttemptsWithSameId_whenComparing_thenAreEqual() {
            // Given
            String sameId = "shared-id";
            Attempt attempt1 = new Attempt(
                    sameId, 5, new BigDecimal("1000"), 0.8, 0.9,
                    AttemptStatus.APPROVED, "task-1", "student-1",
                    LocalDateTime.now(), Collections.emptyList()
            );
            Attempt attempt2 = new Attempt(
                    sameId, 10, new BigDecimal("500"), 0.5, 0.6,
                    AttemptStatus.DISAPPROVED, "task-2", "student-2",
                    LocalDateTime.now().plusDays(1), Collections.emptyList()
            );

            // When & Then
            assertThat(attempt1).isEqualTo(attempt2);
            assertThat(attempt1.hashCode()).isEqualTo(attempt2.hashCode());
        }

        @Test
        @DisplayName("givenTwoAttemptsWithDifferentIds_whenComparing_thenAreNotEqual")
        void givenTwoAttemptsWithDifferentIds_whenComparing_thenAreNotEqual() {
            // Given
            Attempt attempt1 = new Attempt(
                    "id-1", 5, new BigDecimal("1000"), 0.8, 0.9,
                    AttemptStatus.APPROVED, VALID_TASK_ID, VALID_STUDENT_ID,
                    LocalDateTime.now(), Collections.emptyList()
            );
            Attempt attempt2 = new Attempt(
                    "id-2", 5, new BigDecimal("1000"), 0.8, 0.9,
                    AttemptStatus.APPROVED, VALID_TASK_ID, VALID_STUDENT_ID,
                    LocalDateTime.now(), Collections.emptyList()
            );

            // When & Then
            assertThat(attempt1).isNotEqualTo(attempt2);
        }
    }
}
