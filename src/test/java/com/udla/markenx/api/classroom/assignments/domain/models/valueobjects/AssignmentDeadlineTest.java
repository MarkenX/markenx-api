package com.udla.markenx.api.classroom.assignments.domain.models.valueobjects;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.DueDateMustBeInTheFutureException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.DueDateNotProvidedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AssignmentDeadline Value Object")
class AssignmentDeadlineTest {

    @Nested
    @DisplayName("Future deadline creation")
    class FutureDeadlineCreation {

        @Test
        @DisplayName("givenFutureDateTime_whenCreatingFutureDeadline_thenSucceeds")
        void givenFutureDateTime_whenCreatingFutureDeadline_thenSucceeds() {
            // Given
            LocalDateTime futureDate = LocalDateTime.now().plusDays(7);

            // When
            AssignmentDeadline deadline = AssignmentDeadline.future(futureDate);

            // Then
            assertThat(deadline.value()).isEqualTo(futureDate);
        }

        @Test
        @DisplayName("givenPastDateTime_whenCreatingFutureDeadline_thenThrowsDueDateMustBeInTheFutureException")
        void givenPastDateTime_whenCreatingFutureDeadline_thenThrowsDueDateMustBeInTheFutureException() {
            // Given
            LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

            // When & Then
            assertThatThrownBy(() -> AssignmentDeadline.future(pastDate))
                    .isInstanceOf(DueDateMustBeInTheFutureException.class);
        }

        @Test
        @DisplayName("givenCurrentDateTime_whenCreatingFutureDeadline_thenThrowsDueDateMustBeInTheFutureException")
        void givenCurrentDateTime_whenCreatingFutureDeadline_thenThrowsDueDateMustBeInTheFutureException() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When & Then
            assertThatThrownBy(() -> AssignmentDeadline.future(now))
                    .isInstanceOf(DueDateMustBeInTheFutureException.class);
        }
    }

    @Nested
    @DisplayName("Historical deadline creation")
    class HistoricalDeadlineCreation {

        @Test
        @DisplayName("givenPastDateTime_whenCreatingHistoricalDeadline_thenSucceeds")
        void givenPastDateTime_whenCreatingHistoricalDeadline_thenSucceeds() {
            // Given
            LocalDateTime pastDate = LocalDateTime.of(2024, 1, 15, 23, 59);

            // When
            AssignmentDeadline deadline = AssignmentDeadline.historical(pastDate);

            // Then
            assertThat(deadline.value()).isEqualTo(pastDate);
        }

        @Test
        @DisplayName("givenFutureDateTime_whenCreatingHistoricalDeadline_thenSucceeds")
        void givenFutureDateTime_whenCreatingHistoricalDeadline_thenSucceeds() {
            // Given
            LocalDateTime futureDate = LocalDateTime.now().plusMonths(3);

            // When
            AssignmentDeadline deadline = AssignmentDeadline.historical(futureDate);

            // Then
            assertThat(deadline.value()).isEqualTo(futureDate);
        }

        @Test
        @DisplayName("givenNullDateTime_whenCreatingHistoricalDeadline_thenThrowsDueDateNotProvidedException")
        void givenNullDateTime_whenCreatingHistoricalDeadline_thenThrowsDueDateNotProvidedException() {
            // When & Then
            assertThatThrownBy(() -> AssignmentDeadline.historical(null))
                    .isInstanceOf(DueDateNotProvidedException.class);
        }
    }

    @Nested
    @DisplayName("Date and Time accessors")
    class DateTimeAccessors {

        @Test
        @DisplayName("givenDeadline_whenGettingDate_thenReturnsLocalDate")
        void givenDeadline_whenGettingDate_thenReturnsLocalDate() {
            // Given
            LocalDateTime dateTime = LocalDateTime.of(2025, 6, 15, 14, 30);
            AssignmentDeadline deadline = AssignmentDeadline.historical(dateTime);

            // When
            LocalDate date = deadline.date();

            // Then
            assertThat(date).isEqualTo(LocalDate.of(2025, 6, 15));
        }

        @Test
        @DisplayName("givenDeadline_whenGettingTime_thenReturnsLocalTime")
        void givenDeadline_whenGettingTime_thenReturnsLocalTime() {
            // Given
            LocalDateTime dateTime = LocalDateTime.of(2025, 6, 15, 14, 30);
            AssignmentDeadline deadline = AssignmentDeadline.historical(dateTime);

            // When
            LocalTime time = deadline.time();

            // Then
            assertThat(time).isEqualTo(LocalTime.of(14, 30));
        }
    }

    @Nested
    @DisplayName("Overdue check")
    class OverdueCheck {

        @Test
        @DisplayName("givenPastDeadline_whenCheckingIsOverdue_thenReturnsTrue")
        void givenPastDeadline_whenCheckingIsOverdue_thenReturnsTrue() {
            // Given
            LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
            AssignmentDeadline deadline = AssignmentDeadline.historical(pastDate);

            // When
            boolean isOverdue = deadline.isOverdue();

            // Then
            assertThat(isOverdue).isTrue();
        }

        @Test
        @DisplayName("givenFutureDeadline_whenCheckingIsOverdue_thenReturnsFalse")
        void givenFutureDeadline_whenCheckingIsOverdue_thenReturnsFalse() {
            // Given
            LocalDateTime futureDate = LocalDateTime.now().plusDays(7);
            AssignmentDeadline deadline = AssignmentDeadline.future(futureDate);

            // When
            boolean isOverdue = deadline.isOverdue();

            // Then
            assertThat(isOverdue).isFalse();
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoDeadlinesWithSameValue_whenComparing_thenAreEqual")
        void givenTwoDeadlinesWithSameValue_whenComparing_thenAreEqual() {
            // Given
            LocalDateTime dateTime = LocalDateTime.of(2025, 12, 31, 23, 59);
            AssignmentDeadline deadline1 = AssignmentDeadline.historical(dateTime);
            AssignmentDeadline deadline2 = AssignmentDeadline.historical(dateTime);

            // When & Then
            assertThat(deadline1).isEqualTo(deadline2);
            assertThat(deadline1.hashCode()).isEqualTo(deadline2.hashCode());
        }

        @Test
        @DisplayName("givenTwoDeadlinesWithDifferentValues_whenComparing_thenAreNotEqual")
        void givenTwoDeadlinesWithDifferentValues_whenComparing_thenAreNotEqual() {
            // Given
            AssignmentDeadline deadline1 = AssignmentDeadline.historical(LocalDateTime.of(2025, 1, 1, 12, 0));
            AssignmentDeadline deadline2 = AssignmentDeadline.historical(LocalDateTime.of(2025, 1, 2, 12, 0));

            // When & Then
            assertThat(deadline1).isNotEqualTo(deadline2);
        }
    }
}
