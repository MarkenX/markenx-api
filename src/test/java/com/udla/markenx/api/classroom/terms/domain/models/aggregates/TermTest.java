package com.udla.markenx.api.classroom.terms.domain.models.aggregates;

import com.udla.markenx.api.classroom.terms.domain.exceptions.*;
import com.udla.markenx.api.classroom.terms.domain.models.valueobjects.TermStatus;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Term Aggregate")
class TermTest {

    @Nested
    @DisplayName("Factory method createTerm")
    class FactoryMethodCreateTerm {

        @Test
        @DisplayName("givenValidFutureSingleYearTerm_whenCreating_thenSucceeds")
        void givenValidFutureSingleYearTerm_whenCreating_thenSucceeds() {
            // Given
            int year = LocalDate.now().getYear() + 1;
            int sequence = 1;
            LocalDate startDate = LocalDate.now().plusMonths(1);
            LocalDate endDate = startDate.plusMonths(5);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When
            Term term = Term.createTerm(year, sequence, interval);

            // Then
            assertThat(term.getId()).isNotNull();
            assertThat(term.getYear()).isEqualTo(year);
            assertThat(term.getSequence()).isEqualTo(sequence);
            assertThat(term.getStartDate()).isEqualTo(startDate);
            assertThat(term.getEndDate()).isEqualTo(endDate);
            // Future terms start with UPCOMING status, not ACTIVE
            assertThat(term.isUpcoming()).isTrue();
        }

        @Test
        @DisplayName("givenStartDateInPast_whenCreating_thenThrowsTermMustStartInFutureException")
        void givenStartDateInPast_whenCreating_thenThrowsTermMustStartInFutureException() {
            // Given
            int year = LocalDate.now().getYear();
            int sequence = 1;
            LocalDate startDate = LocalDate.now().minusDays(1);
            LocalDate endDate = startDate.plusMonths(5);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When & Then
            assertThatThrownBy(() -> Term.createTerm(year, sequence, interval))
                    .isInstanceOf(TermMustStartInFutureException.class);
        }

        @Test
        @DisplayName("givenEndDateTooFarInFuture_whenCreating_thenThrowsTermCannotBeCreatedTooFarInFutureException")
        void givenEndDateTooFarInFuture_whenCreating_thenThrowsTermCannotBeCreatedTooFarInFutureException() {
            // Given
            int year = LocalDate.now().getYear() + 2;
            int sequence = 1;
            LocalDate startDate = LocalDate.now().plusMonths(1);
            LocalDate endDate = LocalDate.now().plusYears(3);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When & Then
            assertThatThrownBy(() -> Term.createTerm(year, sequence, interval))
                    .isInstanceOf(TermCannotBeCreatedTooFarInFutureException.class);
        }
    }

    @Nested
    @DisplayName("Factory method createHistoricalTerm")
    class FactoryMethodCreateHistoricalTerm {

        @Test
        @DisplayName("givenPastDates_whenCreatingHistoricalTerm_thenSucceeds")
        void givenPastDates_whenCreatingHistoricalTerm_thenSucceeds() {
            // Given
            int year = 2024;
            int sequence = 1;
            LocalDate startDate = LocalDate.of(2024, 3, 1);
            LocalDate endDate = LocalDate.of(2024, 7, 31);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When
            Term term = Term.createHistoricalTerm(year, sequence, interval);

            // Then
            assertThat(term.getId()).isNotNull();
            assertThat(term.getYear()).isEqualTo(year);
            assertThat(term.getStartDate()).isEqualTo(startDate);
            assertThat(term.getEndDate()).isEqualTo(endDate);
        }
    }

    @Nested
    @DisplayName("Year validation")
    class YearValidation {

        @Test
        @DisplayName("givenYearBeforeThreshold_whenCreating_thenThrowsInvalidYearException")
        void givenYearBeforeThreshold_whenCreating_thenThrowsInvalidYearException() {
            // Given
            int invalidYear = 2023;
            LocalDate startDate = LocalDate.of(2023, 3, 1);
            LocalDate endDate = LocalDate.of(2023, 7, 31);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When & Then
            assertThatThrownBy(() -> Term.createHistoricalTerm(invalidYear, 1, interval))
                    .isInstanceOf(InvalidYearException.class);
        }

        @Test
        @DisplayName("givenYearTooFarInFuture_whenCreating_thenThrowsInvalidYearException")
        void givenYearTooFarInFuture_whenCreating_thenThrowsInvalidYearException() {
            // Given
            int invalidYear = LocalDate.now().getYear() + 3;
            LocalDate startDate = LocalDate.now().plusMonths(1);
            LocalDate endDate = startDate.plusMonths(4);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When & Then
            assertThatThrownBy(() -> Term.createHistoricalTerm(invalidYear, 1, interval))
                    .isInstanceOf(InvalidYearException.class);
        }
    }

    @Nested
    @DisplayName("Sequence validation")
    class SequenceValidation {

        @Test
        @DisplayName("givenZeroSequence_whenCreating_thenThrowsInvalidTermSequenceException")
        void givenZeroSequence_whenCreating_thenThrowsInvalidTermSequenceException() {
            // Given
            LocalDate startDate = LocalDate.of(2024, 3, 1);
            LocalDate endDate = LocalDate.of(2024, 7, 31);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When & Then
            assertThatThrownBy(() -> Term.createHistoricalTerm(2024, 0, interval))
                    .isInstanceOf(InvalidTermSequenceException.class);
        }

        @Test
        @DisplayName("givenNegativeSequence_whenCreating_thenThrowsInvalidTermSequenceException")
        void givenNegativeSequence_whenCreating_thenThrowsInvalidTermSequenceException() {
            // Given
            LocalDate startDate = LocalDate.of(2024, 3, 1);
            LocalDate endDate = LocalDate.of(2024, 7, 31);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When & Then
            assertThatThrownBy(() -> Term.createHistoricalTerm(2024, -1, interval))
                    .isInstanceOf(InvalidTermSequenceException.class);
        }

        @Test
        @DisplayName("givenSequenceGreaterThanMax_whenCreating_thenThrowsInvalidTermSequenceException")
        void givenSequenceGreaterThanMax_whenCreating_thenThrowsInvalidTermSequenceException() {
            // Given
            LocalDate startDate = LocalDate.of(2024, 3, 1);
            LocalDate endDate = LocalDate.of(2024, 7, 31);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When & Then (max sequence is 2 based on MAX_MONTHS_LENGTH = 6)
            assertThatThrownBy(() -> Term.createHistoricalTerm(2024, 3, interval))
                    .isInstanceOf(InvalidTermSequenceException.class);
        }
    }

    @Nested
    @DisplayName("Term length validation")
    class TermLengthValidation {

        @Test
        @DisplayName("givenTermTooShort_whenCreating_thenThrowsInvalidTermLengthException")
        void givenTermTooShort_whenCreating_thenThrowsInvalidTermLengthException() {
            // Given
            LocalDate startDate = LocalDate.of(2024, 3, 1);
            LocalDate endDate = LocalDate.of(2024, 5, 1); // Only 2 months
            DateInterval interval = new DateInterval(startDate, endDate);

            // When & Then
            assertThatThrownBy(() -> Term.createHistoricalTerm(2024, 1, interval))
                    .isInstanceOf(InvalidTermLengthException.class);
        }

        @Test
        @DisplayName("givenTermTooLong_whenCreating_thenThrowsInvalidTermLengthException")
        void givenTermTooLong_whenCreating_thenThrowsInvalidTermLengthException() {
            // Given
            LocalDate startDate = LocalDate.of(2024, 1, 1);
            LocalDate endDate = LocalDate.of(2024, 8, 1); // 7 months
            DateInterval interval = new DateInterval(startDate, endDate);

            // When & Then
            assertThatThrownBy(() -> Term.createHistoricalTerm(2024, 1, interval))
                    .isInstanceOf(InvalidTermLengthException.class);
        }
    }

    @Nested
    @DisplayName("Single year term validation")
    class SingleYearTermValidation {

        @Test
        @DisplayName("givenSingleYearInterval_whenCreating_thenSucceeds")
        void givenSingleYearInterval_whenCreating_thenSucceeds() {
            // Given
            LocalDate startDate = LocalDate.of(2024, 3, 1);
            LocalDate endDate = LocalDate.of(2024, 7, 1);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When
            Term term = Term.createHistoricalTerm(2024, 1, interval);

            // Then
            assertThat(term.getStartDate().getYear()).isEqualTo(term.getEndDate().getYear());
        }
    }

    @Nested
    @DisplayName("Cross year term validation")
    class CrossYearTermValidation {

        @Test
        @DisplayName("givenCrossYearInterval_whenCreating_thenSucceeds")
        void givenCrossYearInterval_whenCreating_thenSucceeds() {
            // Given
            LocalDate startDate = LocalDate.of(2024, 10, 1);
            LocalDate endDate = LocalDate.of(2025, 2, 1);
            DateInterval interval = new DateInterval(startDate, endDate);

            // When
            Term term = Term.createHistoricalTerm(2024, 2, interval);

            // Then
            assertThat(term.getStartDate().getYear()).isNotEqualTo(term.getEndDate().getYear());
        }
    }

    @Nested
    @DisplayName("Overlap detection")
    class OverlapDetection {

        @Test
        @DisplayName("givenTwoOverlappingTerms_whenCheckingOverlap_thenReturnsTrue")
        void givenTwoOverlappingTerms_whenCheckingOverlap_thenReturnsTrue() {
            // Given
            Term term1 = Term.createHistoricalTerm(2024, 1,
                    new DateInterval(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1)));
            Term term2 = Term.createHistoricalTerm(2024, 1,
                    new DateInterval(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 10, 1)));

            // When
            boolean overlaps = term1.overlapsWith(term2);

            // Then
            assertThat(overlaps).isTrue();
        }

        @Test
        @DisplayName("givenTwoNonOverlappingTerms_whenCheckingOverlap_thenReturnsFalse")
        void givenTwoNonOverlappingTerms_whenCheckingOverlap_thenReturnsFalse() {
            // Given
            Term term1 = Term.createHistoricalTerm(2024, 1,
                    new DateInterval(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1)));
            Term term2 = Term.createHistoricalTerm(2024, 2,
                    new DateInterval(LocalDate.of(2024, 9, 1), LocalDate.of(2025, 1, 1)));

            // When
            boolean overlaps = term1.overlapsWith(term2);

            // Then
            assertThat(overlaps).isFalse();
        }

        @Test
        @DisplayName("givenNullTerm_whenCheckingOverlap_thenReturnsFalse")
        void givenNullTerm_whenCheckingOverlap_thenReturnsFalse() {
            // Given
            Term term = Term.createHistoricalTerm(2024, 1,
                    new DateInterval(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1)));

            // When
            boolean overlaps = term.overlapsWith(null);

            // Then
            assertThat(overlaps).isFalse();
        }
    }

    @Nested
    @DisplayName("Status checks")
    class StatusChecks {

        @Test
        @DisplayName("givenUpcomingTerm_whenCheckingIsUpcoming_thenReturnsTrue")
        void givenUpcomingTerm_whenCheckingIsUpcoming_thenReturnsTrue() {
            // Given
            Term term = new Term(
                    "id", LifecycleStatus.ACTIVE,
                    LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(6),
                    LocalDate.now().getYear(), 1, TermStatus.UPCOMING
            );

            // When & Then
            assertThat(term.isUpcoming()).isTrue();
            assertThat(term.isActive()).isFalse();
            assertThat(term.hasEnded()).isFalse();
        }

        @Test
        @DisplayName("givenActiveTerm_whenCheckingIsActive_thenReturnsTrue")
        void givenActiveTerm_whenCheckingIsActive_thenReturnsTrue() {
            // Given
            Term term = new Term(
                    "id", LifecycleStatus.ACTIVE,
                    LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(3),
                    LocalDate.now().getYear(), 1, TermStatus.ACTIVE
            );

            // When & Then
            assertThat(term.isActive()).isTrue();
            assertThat(term.isUpcoming()).isFalse();
            assertThat(term.hasEnded()).isFalse();
        }

        @Test
        @DisplayName("givenEndedTerm_whenCheckingHasEnded_thenReturnsTrue")
        void givenEndedTerm_whenCheckingHasEnded_thenReturnsTrue() {
            // Given
            Term term = new Term(
                    "id", LifecycleStatus.ACTIVE,
                    LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1),
                    2024, 1, TermStatus.ENDED
            );

            // When & Then
            assertThat(term.hasEnded()).isTrue();
            assertThat(term.isActive()).isFalse();
            assertThat(term.isUpcoming()).isFalse();
        }
    }

    @Nested
    @DisplayName("Contains date")
    class ContainsDate {

        @Test
        @DisplayName("givenDateWithinTerm_whenCheckingContains_thenReturnsTrue")
        void givenDateWithinTerm_whenCheckingContains_thenReturnsTrue() {
            // Given
            Term term = Term.createHistoricalTerm(2024, 1,
                    new DateInterval(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1)));
            LocalDate dateToCheck = LocalDate.of(2024, 5, 15);

            // When
            boolean contains = term.containsDate(dateToCheck);

            // Then
            assertThat(contains).isTrue();
        }

        @Test
        @DisplayName("givenDateOutsideTerm_whenCheckingContains_thenReturnsFalse")
        void givenDateOutsideTerm_whenCheckingContains_thenReturnsFalse() {
            // Given
            Term term = Term.createHistoricalTerm(2024, 1,
                    new DateInterval(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1)));
            LocalDate dateToCheck = LocalDate.of(2024, 8, 15);

            // When
            boolean contains = term.containsDate(dateToCheck);

            // Then
            assertThat(contains).isFalse();
        }
    }

    @Nested
    @DisplayName("Duration")
    class Duration {

        @Test
        @DisplayName("givenTerm_whenGettingDurationInMonths_thenReturnsCorrectValue")
        void givenTerm_whenGettingDurationInMonths_thenReturnsCorrectValue() {
            // Given
            Term term = Term.createHistoricalTerm(2024, 1,
                    new DateInterval(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 8, 1)));

            // When
            long duration = term.getDurationInMonths();

            // Then
            assertThat(duration).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoTermsWithSameId_whenComparing_thenAreEqual")
        void givenTwoTermsWithSameId_whenComparing_thenAreEqual() {
            // Given
            String sameId = "shared-id";
            Term term1 = new Term(sameId, LifecycleStatus.ACTIVE,
                    LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1),
                    2024, 1, TermStatus.ACTIVE);
            Term term2 = new Term(sameId, LifecycleStatus.ACTIVE,
                    LocalDate.of(2024, 9, 1), LocalDate.of(2025, 1, 1),
                    2024, 2, TermStatus.UPCOMING);

            // When & Then
            assertThat(term1).isEqualTo(term2);
            assertThat(term1.hashCode()).isEqualTo(term2.hashCode());
        }

        @Test
        @DisplayName("givenTwoTermsWithDifferentIds_whenComparing_thenAreNotEqual")
        void givenTwoTermsWithDifferentIds_whenComparing_thenAreNotEqual() {
            // Given
            Term term1 = new Term("id-1", LifecycleStatus.ACTIVE,
                    LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1),
                    2024, 1, TermStatus.ACTIVE);
            Term term2 = new Term("id-2", LifecycleStatus.ACTIVE,
                    LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1),
                    2024, 1, TermStatus.ACTIVE);

            // When & Then
            assertThat(term1).isNotEqualTo(term2);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringBehavior {

        @Test
        @DisplayName("givenTerm_whenCallingToString_thenReturnsYearAndSequence")
        void givenTerm_whenCallingToString_thenReturnsYearAndSequence() {
            // Given
            Term term = Term.createHistoricalTerm(2024, 1,
                    new DateInterval(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1)));

            // When
            String result = term.toString();

            // Then
            assertThat(result).isEqualTo("2024-1");
        }
    }
}
