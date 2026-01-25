package com.udla.markenx.api.classroom.assignments.domain.models.aggregates;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.*;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Task Aggregate")
class TaskTest {

    private static final String VALID_COURSE_ID = "course-123";
    private static final String VALID_SCENARIO_ID = "scenario-456";
    private static final int VALID_MAX_ATTEMPTS = 3;
    private static final double VALID_MIN_SCORE = 0.7;

    @Nested
    @DisplayName("Factory method create")
    class FactoryMethodCreate {

        @Test
        @DisplayName("givenValidParameters_whenCreatingTask_thenSucceeds")
        void givenValidParameters_whenCreatingTask_thenSucceeds() {
            // Given
            AssignmentInfo info = new AssignmentInfo("Marketing Task", "Complete the marketing simulation");
            LocalDateTime deadline = LocalDateTime.now().plusDays(7);
            AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);

            // When
            Task task = Task.create(info, deadline, minScore, VALID_COURSE_ID, VALID_MAX_ATTEMPTS, VALID_SCENARIO_ID);

            // Then
            assertThat(task.getId()).isNotNull();
            assertThat(task.getInfo()).isEqualTo(info);
            assertThat(task.getMaxAttempts()).isEqualTo(VALID_MAX_ATTEMPTS);
            assertThat(task.getScenarioId()).isEqualTo(VALID_SCENARIO_ID);
            assertThat(task.getCourseId()).isEqualTo(VALID_COURSE_ID);
            assertThat(task.isActive()).isTrue();
        }

        @Test
        @DisplayName("givenPastDeadline_whenCreatingTask_thenThrowsDueDateMustBeInTheFutureException")
        void givenPastDeadline_whenCreatingTask_thenThrowsDueDateMustBeInTheFutureException() {
            // Given
            AssignmentInfo info = new AssignmentInfo("Task Title", "Task Summary");
            LocalDateTime pastDeadline = LocalDateTime.now().minusDays(1);
            AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);

            // When & Then
            assertThatThrownBy(() -> Task.create(info, pastDeadline, minScore, VALID_COURSE_ID, VALID_MAX_ATTEMPTS, VALID_SCENARIO_ID))
                    .isInstanceOf(DueDateMustBeInTheFutureException.class);
        }
    }

    @Nested
    @DisplayName("Factory method createHistorical")
    class FactoryMethodCreateHistorical {

        @Test
        @DisplayName("givenPastDeadline_whenCreatingHistoricalTask_thenSucceeds")
        void givenPastDeadline_whenCreatingHistoricalTask_thenSucceeds() {
            // Given
            AssignmentInfo info = new AssignmentInfo("Past Task", "Historical task summary");
            LocalDateTime pastDeadline = LocalDateTime.of(2024, 6, 15, 23, 59);
            AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);

            // When
            Task task = Task.createHistorical(info, pastDeadline, minScore, VALID_COURSE_ID, VALID_MAX_ATTEMPTS, VALID_SCENARIO_ID);

            // Then
            assertThat(task.getId()).isNotNull();
            assertThat(task.getDeadline().value()).isEqualTo(pastDeadline);
        }
    }

    @Nested
    @DisplayName("Max attempts validation")
    class MaxAttemptsValidation {

        @Test
        @DisplayName("givenZeroMaxAttempts_whenCreatingTask_thenThrowsInvalidMaxAttemptsException")
        void givenZeroMaxAttempts_whenCreatingTask_thenThrowsInvalidMaxAttemptsException() {
            // Given
            AssignmentInfo info = new AssignmentInfo("Title", "Summary");
            LocalDateTime deadline = LocalDateTime.now().plusDays(7);
            AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);

            // When & Then
            assertThatThrownBy(() -> Task.create(info, deadline, minScore, VALID_COURSE_ID, 0, VALID_SCENARIO_ID))
                    .isInstanceOf(InvalidMaxAttemptsException.class);
        }

        @Test
        @DisplayName("givenNegativeMaxAttempts_whenCreatingTask_thenThrowsInvalidMaxAttemptsException")
        void givenNegativeMaxAttempts_whenCreatingTask_thenThrowsInvalidMaxAttemptsException() {
            // Given
            AssignmentInfo info = new AssignmentInfo("Title", "Summary");
            LocalDateTime deadline = LocalDateTime.now().plusDays(7);
            AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);

            // When & Then
            assertThatThrownBy(() -> Task.create(info, deadline, minScore, VALID_COURSE_ID, -1, VALID_SCENARIO_ID))
                    .isInstanceOf(InvalidMaxAttemptsException.class);
        }
    }

    @Nested
    @DisplayName("Scenario ID validation")
    class ScenarioIdValidation {

        @Test
        @DisplayName("givenNullScenarioId_whenCreatingTask_thenThrowsInvalidScenarioIdException")
        void givenNullScenarioId_whenCreatingTask_thenThrowsInvalidScenarioIdException() {
            // Given
            AssignmentInfo info = new AssignmentInfo("Title", "Summary");
            LocalDateTime deadline = LocalDateTime.now().plusDays(7);
            AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);

            // When & Then
            assertThatThrownBy(() -> Task.create(info, deadline, minScore, VALID_COURSE_ID, VALID_MAX_ATTEMPTS, null))
                    .isInstanceOf(InvalidScenarioIdException.class);
        }

        @Test
        @DisplayName("givenBlankScenarioId_whenCreatingTask_thenThrowsInvalidScenarioIdException")
        void givenBlankScenarioId_whenCreatingTask_thenThrowsInvalidScenarioIdException() {
            // Given
            AssignmentInfo info = new AssignmentInfo("Title", "Summary");
            LocalDateTime deadline = LocalDateTime.now().plusDays(7);
            AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);

            // When & Then
            assertThatThrownBy(() -> Task.create(info, deadline, minScore, VALID_COURSE_ID, VALID_MAX_ATTEMPTS, "   "))
                    .isInstanceOf(InvalidScenarioIdException.class);
        }
    }

    @Nested
    @DisplayName("Course ID validation")
    class CourseIdValidation {

        @Test
        @DisplayName("givenNullCourseId_whenCreatingTask_thenThrowsInvalidCourseIdException")
        void givenNullCourseId_whenCreatingTask_thenThrowsInvalidCourseIdException() {
            // Given
            AssignmentInfo info = new AssignmentInfo("Title", "Summary");
            LocalDateTime deadline = LocalDateTime.now().plusDays(7);
            AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);

            // When & Then
            assertThatThrownBy(() -> Task.create(info, deadline, minScore, null, VALID_MAX_ATTEMPTS, VALID_SCENARIO_ID))
                    .isInstanceOf(InvalidCourseIdException.class);
        }

        @Test
        @DisplayName("givenBlankCourseId_whenCreatingTask_thenThrowsInvalidCourseIdException")
        void givenBlankCourseId_whenCreatingTask_thenThrowsInvalidCourseIdException() {
            // Given
            AssignmentInfo info = new AssignmentInfo("Title", "Summary");
            LocalDateTime deadline = LocalDateTime.now().plusDays(7);
            AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);

            // When & Then
            assertThatThrownBy(() -> Task.create(info, deadline, minScore, "  ", VALID_MAX_ATTEMPTS, VALID_SCENARIO_ID))
                    .isInstanceOf(InvalidCourseIdException.class);
        }
    }

    @Nested
    @DisplayName("Setters")
    class Setters {

        @Test
        @DisplayName("givenValidMaxAttempts_whenSettingMaxAttempts_thenUpdatesValue")
        void givenValidMaxAttempts_whenSettingMaxAttempts_thenUpdatesValue() {
            // Given
            Task task = createValidTask();

            // When
            task.setMaxAttempts(5);

            // Then
            assertThat(task.getMaxAttempts()).isEqualTo(5);
        }

        @Test
        @DisplayName("givenZeroMaxAttempts_whenSettingMaxAttempts_thenThrowsInvalidMaxAttemptsException")
        void givenZeroMaxAttempts_whenSettingMaxAttempts_thenThrowsInvalidMaxAttemptsException() {
            // Given
            Task task = createValidTask();

            // When & Then
            assertThatThrownBy(() -> task.setMaxAttempts(0))
                    .isInstanceOf(InvalidMaxAttemptsException.class);
        }
    }

    @Nested
    @DisplayName("Reconstruction from persistence")
    class ReconstructionFromPersistence {

        @Test
        @DisplayName("givenPersistedData_whenReconstructingTask_thenAllFieldsAreSet")
        void givenPersistedData_whenReconstructingTask_thenAllFieldsAreSet() {
            // Given
            String id = "task-attemptId-123";
            LifecycleStatus lifecycleStatus = LifecycleStatus.ACTIVE;
            long code = 1001L;
            String title = "Reconstructed Task";
            String summary = "Task reconstructed from database";
            LocalDateTime deadline = LocalDateTime.of(2025, 12, 31, 23, 59);
            double minScoreToPass = 0.8;
            String courseId = "course-abc";
            int maxAttempts = 5;
            String scenarioId = "scenario-xyz";

            // When
            Task task = new Task(id, lifecycleStatus, code, title, summary, deadline, minScoreToPass, courseId, maxAttempts, scenarioId);

            // Then
            assertThat(task.getId()).isEqualTo(id);
            assertThat(task.getInfo().title()).isEqualTo(title);
            assertThat(task.getInfo().summary()).isEqualTo(summary);
            assertThat(task.getMaxAttempts()).isEqualTo(maxAttempts);
            assertThat(task.getScenarioId()).isEqualTo(scenarioId);
            assertThat(task.getCourseId()).isEqualTo(courseId);
            assertThat(task.isActive()).isTrue();
        }

        @Test
        @DisplayName("givenInvalidCode_whenReconstructingTask_thenThrowsInvalidAssignmentCodeException")
        void givenInvalidCode_whenReconstructingTask_thenThrowsInvalidAssignmentCodeException() {
            // When & Then
            assertThatThrownBy(() -> new Task(
                    "id", LifecycleStatus.ACTIVE, 0L, "Title", "Summary",
                    LocalDateTime.now(), 0.7, "course", 3, "scenario"
            )).isInstanceOf(InvalidAssignmentCodeException.class);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringBehavior {

        @Test
        @DisplayName("givenTask_whenCallingToString_thenReturnsFormattedCode")
        void givenTask_whenCallingToString_thenReturnsFormattedCode() {
            // Given
            Task task = new Task(
                    "id", LifecycleStatus.ACTIVE, 42L, "Title", "Summary",
                    LocalDateTime.now().plusDays(7), 0.7, "course", 3, "scenario"
            );

            // When
            String result = task.toString();

            // Then
            assertThat(result).isEqualTo("TSK-0042");
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoTasksWithSameId_whenComparing_thenAreEqual")
        void givenTwoTasksWithSameId_whenComparing_thenAreEqual() {
            // Given
            String sameId = "shared-attemptId";
            Task task1 = new Task(sameId, LifecycleStatus.ACTIVE, 1L, "Title 1", "Summary 1",
                    LocalDateTime.now().plusDays(7), 0.7, "course-1", 3, "scenario-1");
            Task task2 = new Task(sameId, LifecycleStatus.ACTIVE, 2L, "Title 2", "Summary 2",
                    LocalDateTime.now().plusDays(14), 0.8, "course-2", 5, "scenario-2");

            // When & Then
            assertThat(task1).isEqualTo(task2);
            assertThat(task1.hashCode()).isEqualTo(task2.hashCode());
        }

        @Test
        @DisplayName("givenTwoTasksWithDifferentIds_whenComparing_thenAreNotEqual")
        void givenTwoTasksWithDifferentIds_whenComparing_thenAreNotEqual() {
            // Given
            Task task1 = new Task("attemptId-1", LifecycleStatus.ACTIVE, 1L, "Title", "Summary",
                    LocalDateTime.now().plusDays(7), 0.7, "course", 3, "scenario");
            Task task2 = new Task("attemptId-2", LifecycleStatus.ACTIVE, 1L, "Title", "Summary",
                    LocalDateTime.now().plusDays(7), 0.7, "course", 3, "scenario");

            // When & Then
            assertThat(task1).isNotEqualTo(task2);
        }
    }

    private Task createValidTask() {
        AssignmentInfo info = new AssignmentInfo("Title", "Summary");
        LocalDateTime deadline = LocalDateTime.now().plusDays(7);
        AssignmentScore minScore = new AssignmentScore(VALID_MIN_SCORE);
        return Task.create(info, deadline, minScore, VALID_COURSE_ID, VALID_MAX_ATTEMPTS, VALID_SCENARIO_ID);
    }
}
