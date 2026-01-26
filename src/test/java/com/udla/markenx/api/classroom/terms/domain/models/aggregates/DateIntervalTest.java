package com.udla.markenx.api.classroom.terms.domain.models.aggregates;

import com.udla.markenx.api.classroom.terms.domain.exceptions.EndDateCannotBeNullException;
import com.udla.markenx.api.classroom.terms.domain.exceptions.InvalidDateIntervalException;
import com.udla.markenx.api.classroom.terms.domain.exceptions.StartDateCannotBeNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("DateInterval Value Object")
class DateIntervalTest {

    @Nested
    @DisplayName("Constructor validation")
    class ConstructorValidation {

        @Test
        @DisplayName("givenValidDates_whenCreatingDateInterval_thenSucceeds")
        void givenValidDates_whenCreatingDateInterval_thenSucceeds() {
            // Given
            LocalDate startDate = LocalDate.of(2025, 3, 1);
            LocalDate endDate = LocalDate.of(2025, 7, 31);

            // When
            DateInterval interval = new DateInterval(startDate, endDate);

            // Then
            assertThat(interval.getStartDate()).isEqualTo(startDate);
            assertThat(interval.getEndDate()).isEqualTo(endDate);
        }

        @Test
        @DisplayName("givenNullStartDate_whenCreatingDateInterval_thenThrowsStartDateCannotBeNullException")
        void givenNullStartDate_whenCreatingDateInterval_thenThrowsStartDateCannotBeNullException() {
            // Given
            LocalDate endDate = LocalDate.of(2025, 7, 31);

            // When & Then
            assertThatThrownBy(() -> new DateInterval(null, endDate))
                    .isInstanceOf(StartDateCannotBeNullException.class);
        }

        @Test
        @DisplayName("givenNullEndDate_whenCreatingDateInterval_thenThrowsEndDateCannotBeNullException")
        void givenNullEndDate_whenCreatingDateInterval_thenThrowsEndDateCannotBeNullException() {
            // Given
            LocalDate startDate = LocalDate.of(2025, 3, 1);

            // When & Then
            assertThatThrownBy(() -> new DateInterval(startDate, null))
                    .isInstanceOf(EndDateCannotBeNullException.class);
        }

        @Test
        @DisplayName("givenEndDateBeforeStartDate_whenCreatingDateInterval_thenThrowsInvalidDateIntervalException")
        void givenEndDateBeforeStartDate_whenCreatingDateInterval_thenThrowsInvalidDateIntervalException() {
            // Given
            LocalDate startDate = LocalDate.of(2025, 7, 31);
            LocalDate endDate = LocalDate.of(2025, 3, 1);

            // When & Then
            assertThatThrownBy(() -> new DateInterval(startDate, endDate))
                    .isInstanceOf(InvalidDateIntervalException.class);
        }

        @Test
        @DisplayName("givenSameStartAndEndDate_whenCreatingDateInterval_thenSucceeds")
        void givenSameStartAndEndDate_whenCreatingDateInterval_thenSucceeds() {
            // Given
            LocalDate date = LocalDate.of(2025, 5, 15);

            // When
            DateInterval interval = new DateInterval(date, date);

            // Then
            assertThat(interval.getStartDate()).isEqualTo(date);
            assertThat(interval.getEndDate()).isEqualTo(date);
        }
    }

    @Nested
    @DisplayName("Year span detection")
    class YearSpanDetection {

        @Test
        @DisplayName("givenIntervalWithinSameYear_whenCheckingSpansOneYear_thenReturnsTrue")
        void givenIntervalWithinSameYear_whenCheckingSpansOneYear_thenReturnsTrue() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );

            // When & Then
            assertThat(interval.spansOneYear()).isTrue();
            assertThat(interval.spansMultipleYears()).isFalse();
        }

        @Test
        @DisplayName("givenIntervalAcrossTwoYears_whenCheckingSpansMultipleYears_thenReturnsTrue")
        void givenIntervalAcrossTwoYears_whenCheckingSpansMultipleYears_thenReturnsTrue() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 10, 1),
                    LocalDate.of(2026, 2, 28)
            );

            // When & Then
            assertThat(interval.spansMultipleYears()).isTrue();
            assertThat(interval.spansOneYear()).isFalse();
        }
    }

    @Nested
    @DisplayName("Month length calculation")
    class MonthLengthCalculation {

        @Test
        @DisplayName("givenFourMonthInterval_whenGettingMonthLength_thenReturnsFour")
        void givenFourMonthInterval_whenGettingMonthLength_thenReturnsFour() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 1)
            );

            // When
            long monthLength = interval.getMonthLength();

            // Then
            assertThat(monthLength).isEqualTo(4);
        }

        @Test
        @DisplayName("givenSixMonthInterval_whenGettingMonthLength_thenReturnsSix")
        void givenSixMonthInterval_whenGettingMonthLength_thenReturnsSix() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 7, 1)
            );

            // When
            long monthLength = interval.getMonthLength();

            // Then
            assertThat(monthLength).isEqualTo(6);
        }
    }

    @Nested
    @DisplayName("Overlap detection")
    class OverlapDetection {

        @Test
        @DisplayName("givenTwoOverlappingIntervals_whenCheckingOverlap_thenReturnsTrue")
        void givenTwoOverlappingIntervals_whenCheckingOverlap_thenReturnsTrue() {
            // Given
            DateInterval interval1 = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            DateInterval interval2 = new DateInterval(
                    LocalDate.of(2025, 6, 1),
                    LocalDate.of(2025, 10, 31)
            );

            // When
            boolean overlaps = interval1.overlapsWith(interval2);

            // Then
            assertThat(overlaps).isTrue();
        }

        @Test
        @DisplayName("givenTwoNonOverlappingIntervals_whenCheckingOverlap_thenReturnsFalse")
        void givenTwoNonOverlappingIntervals_whenCheckingOverlap_thenReturnsFalse() {
            // Given
            DateInterval interval1 = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 6, 30)
            );
            DateInterval interval2 = new DateInterval(
                    LocalDate.of(2025, 8, 1),
                    LocalDate.of(2025, 11, 30)
            );

            // When
            boolean overlaps = interval1.overlapsWith(interval2);

            // Then
            assertThat(overlaps).isFalse();
        }

        @Test
        @DisplayName("givenTwoIdenticalIntervals_whenCheckingOverlap_thenReturnsTrue")
        void givenTwoIdenticalIntervals_whenCheckingOverlap_thenReturnsTrue() {
            // Given
            DateInterval interval1 = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            DateInterval interval2 = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );

            // When
            boolean overlaps = interval1.overlapsWith(interval2);

            // Then
            assertThat(overlaps).isTrue();
        }

        @Test
        @DisplayName("givenSecondIntervalContainedInFirst_whenCheckingOverlap_thenReturnsTrue")
        void givenSecondIntervalContainedInFirst_whenCheckingOverlap_thenReturnsTrue() {
            // Given
            DateInterval interval1 = new DateInterval(
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 12, 31)
            );
            DateInterval interval2 = new DateInterval(
                    LocalDate.of(2025, 4, 1),
                    LocalDate.of(2025, 8, 31)
            );

            // When
            boolean overlaps = interval1.overlapsWith(interval2);

            // Then
            assertThat(overlaps).isTrue();
        }
    }

    @Nested
    @DisplayName("Contains date")
    class ContainsDate {

        @Test
        @DisplayName("givenDateWithinInterval_whenCheckingContains_thenReturnsTrue")
        void givenDateWithinInterval_whenCheckingContains_thenReturnsTrue() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            LocalDate dateToCheck = LocalDate.of(2025, 5, 15);

            // When
            boolean contains = interval.contains(dateToCheck);

            // Then
            assertThat(contains).isTrue();
        }

        @Test
        @DisplayName("givenDateEqualToStartDate_whenCheckingContains_thenReturnsTrue")
        void givenDateEqualToStartDate_whenCheckingContains_thenReturnsTrue() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            LocalDate dateToCheck = LocalDate.of(2025, 3, 1);

            // When
            boolean contains = interval.contains(dateToCheck);

            // Then
            assertThat(contains).isTrue();
        }

        @Test
        @DisplayName("givenDateEqualToEndDate_whenCheckingContains_thenReturnsTrue")
        void givenDateEqualToEndDate_whenCheckingContains_thenReturnsTrue() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            LocalDate dateToCheck = LocalDate.of(2025, 7, 31);

            // When
            boolean contains = interval.contains(dateToCheck);

            // Then
            assertThat(contains).isTrue();
        }

        @Test
        @DisplayName("givenDateBeforeInterval_whenCheckingContains_thenReturnsFalse")
        void givenDateBeforeInterval_whenCheckingContains_thenReturnsFalse() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            LocalDate dateToCheck = LocalDate.of(2025, 2, 15);

            // When
            boolean contains = interval.contains(dateToCheck);

            // Then
            assertThat(contains).isFalse();
        }

        @Test
        @DisplayName("givenDateAfterInterval_whenCheckingContains_thenReturnsFalse")
        void givenDateAfterInterval_whenCheckingContains_thenReturnsFalse() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            LocalDate dateToCheck = LocalDate.of(2025, 8, 15);

            // When
            boolean contains = interval.contains(dateToCheck);

            // Then
            assertThat(contains).isFalse();
        }
    }

    @Nested
    @DisplayName("Setters")
    class Setters {

        @Test
        @DisplayName("givenValidNewStartDate_whenSettingStartDate_thenUpdatesValue")
        void givenValidNewStartDate_whenSettingStartDate_thenUpdatesValue() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            LocalDate newStartDate = LocalDate.of(2025, 2, 1);

            // When
            interval.setStartDate(newStartDate);

            // Then
            assertThat(interval.getStartDate()).isEqualTo(newStartDate);
        }

        @Test
        @DisplayName("givenValidNewEndDate_whenSettingEndDate_thenUpdatesValue")
        void givenValidNewEndDate_whenSettingEndDate_thenUpdatesValue() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            LocalDate newEndDate = LocalDate.of(2025, 8, 31);

            // When
            interval.setEndDate(newEndDate);

            // Then
            assertThat(interval.getEndDate()).isEqualTo(newEndDate);
        }

        @Test
        @DisplayName("givenStartDateAfterEndDate_whenSettingStartDate_thenThrowsInvalidDateIntervalException")
        void givenStartDateAfterEndDate_whenSettingStartDate_thenThrowsInvalidDateIntervalException() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            LocalDate invalidStartDate = LocalDate.of(2025, 9, 1);

            // When & Then
            assertThatThrownBy(() -> interval.setStartDate(invalidStartDate))
                    .isInstanceOf(InvalidDateIntervalException.class);
        }

        @Test
        @DisplayName("givenEndDateBeforeStartDate_whenSettingEndDate_thenThrowsInvalidDateIntervalException")
        void givenEndDateBeforeStartDate_whenSettingEndDate_thenThrowsInvalidDateIntervalException() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            LocalDate invalidEndDate = LocalDate.of(2025, 2, 1);

            // When & Then
            assertThatThrownBy(() -> interval.setEndDate(invalidEndDate))
                    .isInstanceOf(InvalidDateIntervalException.class);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoIntervalsWithSameDates_whenComparing_thenAreEqual")
        void givenTwoIntervalsWithSameDates_whenComparing_thenAreEqual() {
            // Given
            DateInterval interval1 = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            DateInterval interval2 = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );

            // When & Then
            assertThat(interval1).isEqualTo(interval2);
            assertThat(interval1.hashCode()).isEqualTo(interval2.hashCode());
        }

        @Test
        @DisplayName("givenTwoIntervalsWithDifferentDates_whenComparing_thenAreNotEqual")
        void givenTwoIntervalsWithDifferentDates_whenComparing_thenAreNotEqual() {
            // Given
            DateInterval interval1 = new DateInterval(
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31)
            );
            DateInterval interval2 = new DateInterval(
                    LocalDate.of(2025, 4, 1),
                    LocalDate.of(2025, 8, 31)
            );

            // When & Then
            assertThat(interval1).isNotEqualTo(interval2);
        }
    }

    @Nested
    @DisplayName("End year accessor")
    class EndYearAccessor {

        @Test
        @DisplayName("givenInterval_whenGettingEndYear_thenReturnsCorrectYear")
        void givenInterval_whenGettingEndYear_thenReturnsCorrectYear() {
            // Given
            DateInterval interval = new DateInterval(
                    LocalDate.of(2025, 10, 1),
                    LocalDate.of(2026, 2, 28)
            );

            // When
            int endYear = interval.getEndYear();

            // Then
            assertThat(endYear).isEqualTo(2026);
        }
    }
}
