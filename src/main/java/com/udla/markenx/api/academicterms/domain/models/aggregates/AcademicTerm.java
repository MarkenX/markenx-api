package com.udla.markenx.api.academicterms.domain.models.aggregates;

import com.udla.markenx.api.academicterms.domain.exceptions.*;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.DateInterval;
import com.udla.markenx.api.academicterms.domain.utils.DateUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Getter
public class AcademicTerm {

    private static final int MIN_MONTHS_LENGTH = 4;
    private static final int MAX_MONTHS_LENGTH = 6;
    private static final int MIN_MONTHS_PER_YEAR = 2;

    private final AcademicTermId id;
    private final int year;
    private final int sequence;

    @Getter(AccessLevel.NONE)
    private final DateInterval dateInterval;

    private AcademicTermStatus status;

    private AcademicTerm(AcademicTermId id, DateInterval dateInterval, int year, int sequence) {
        this.id = id;
        this.year = validateYear(year);
        this.sequence = validateSequence(sequence);
        this.dateInterval = dateInterval;

        validateCommonInvariants(dateInterval);
        this.status = calculateStatus();
    }

    /**
     * Crea un término académico que abarca un solo año calendario
     */
    @Contract("_, _, _ -> new")
    public static @NotNull AcademicTerm createSingleYearTerm(int year, int sequence, DateInterval dateInterval) {
        validateSingleYear(dateInterval);

        var id = AcademicTermId.generate();
        return new AcademicTerm(id, dateInterval, year, sequence);
    }

    /**
     * Crea un término académico que cruza dos años calendario
     */
    @Contract("_, _, _ -> new")
    public static @NotNull AcademicTerm createCrossYearTerm(int year, int sequence, DateInterval dateInterval) {
        validateCrossYears(dateInterval);
        validateCrossYearMonths(dateInterval);

        var id = AcademicTermId.generate();
        return new AcademicTerm(id, dateInterval, year, sequence);
    }

    /**
     * Reconstruye un término desde persistencia (para el repositorio)
     */
    public static @NotNull AcademicTerm reconstituteFrom(
            String id,
            DateInterval dateInterval,
            int year,
            int sequence,
            AcademicTermStatus status) {

        var term = new AcademicTerm(new AcademicTermId(id), dateInterval, year, sequence);
        term.status = status; // Override del estado calculado
        return term;
    }

    private int validateYear(int year) {
        var nextYear = LocalDate.now().getYear() + 1;
        if (year < 1 || year > nextYear) {
            throw new InvalidAcademicYearException(year, nextYear);
        }
        return year;
    }

    private int validateSequence(int sequence) {
        if (sequence <= 0) {
            throw new InvalidTermSequenceException(sequence);
        }
        return sequence;
    }

    private void validateCommonInvariants(DateInterval dateInterval) {
        validateStartDateInFuture(dateInterval);
        validateEndDateNotTooFar(dateInterval);
        validateMonthLength(dateInterval);
    }

    private static void validateSingleYear(@NotNull DateInterval interval) {
        if (interval.spansMultipleYears()) {
            throw new TermMustBeWithinSingleYearException();
        }
    }

    /**
     * Validates whether the given date interval spans multiple calendar years.
     * Throws an exception if the interval is confined to a single calendar year.
     *
     * @param interval the date interval to validate, must not be null
     * @throws TermMustSpanTwoYearsException if the date interval does not span across two calendar years
     */
    private static void validateCrossYears(@NotNull DateInterval interval) {
        if (interval.spansOneYear()) {
            throw new TermMustSpanTwoYearsException();
        }
    }

    private void validateStartDateInFuture(@NotNull DateInterval interval) {
        if (!LocalDate.now().isBefore(interval.startDate())) {
            throw new TermMustStartInFutureException(interval.startDate());
        }
    }

    private void validateEndDateNotTooFar(@NotNull DateInterval interval) {
        var maxAllowedYear = LocalDate.now().getYear() + 1;
        if (interval.getEndYear() > maxAllowedYear) {
            throw new TermCannotBeCreatedTooFarInFutureException(
                    interval.getEndYear(),
                    maxAllowedYear
            );
        }
    }

    private void validateMonthLength(@NotNull DateInterval interval) {
        long monthLength = interval.getMonthLength();

        if (monthLength < MIN_MONTHS_LENGTH || monthLength > MAX_MONTHS_LENGTH) {
            throw new InvalidTermLengthException(
                    monthLength,
                    MIN_MONTHS_LENGTH,
                    MAX_MONTHS_LENGTH
            );
        }
    }

    /**
     * Validates whether the given date interval satisfies the minimum required number of months
     * at the end of the starting year and at the beginning of the ending year. Throws an exception
     * if either condition is not met.
     *
     * @param interval the date interval to validate, must not be null
     * @throws InsufficientMonthsBeforeYearEndException if the number of months from the start date to the end of the year is less than the minimum required
     * @throws InsufficientMonthsAfterYearStartException if the number of months from the start of the year to the end date is less than the minimum required
     */
    private static void validateCrossYearMonths(@NotNull DateInterval interval) {
        long monthsAtStart = DateUtils.monthsToEndOfYear(interval.startDate());
        long monthsAtEnd = DateUtils.monthsFromStartOfYear(interval.endDate());

        if (monthsAtStart < MIN_MONTHS_PER_YEAR) {
            throw new InsufficientMonthsBeforeYearEndException(
                    monthsAtStart,
                    MIN_MONTHS_PER_YEAR
            );
        }

        if (monthsAtEnd < MIN_MONTHS_PER_YEAR) {
            throw new InsufficientMonthsAfterYearStartException(
                    monthsAtEnd,
                    MIN_MONTHS_PER_YEAR
            );
        }
    }

    public LocalDate getStartDate() {
        return this.dateInterval.startDate();
    }

    public LocalDate getEndDate() {
        return this.dateInterval.endDate();
    }

    public boolean overlapsWith(AcademicTerm other) {
        if (other == null) {
            return false;
        }
        return this.dateInterval.overlapsWith(other.dateInterval);
    }

    public boolean isActive() {
        return this.status == AcademicTermStatus.ACTIVE;
    }

    public boolean isUpcoming() {
        return this.status == AcademicTermStatus.UPCOMING;
    }

    public boolean hasEnded() {
        return this.status == AcademicTermStatus.ENDED;
    }

    public void refreshStatus() {
        AcademicTermStatus newStatus = calculateStatus();
        if (this.status != newStatus) {
            this.status = newStatus;
        }
    }

    private AcademicTermStatus calculateStatus() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = dateInterval.startDate();
        LocalDate endDate = dateInterval.endDate();

        if (today.isAfter(startDate) && today.isBefore(endDate)) {
            return AcademicTermStatus.ACTIVE;
        }
        if (today.isBefore(startDate)) {
            return AcademicTermStatus.UPCOMING;
        }
        return AcademicTermStatus.ENDED;
    }

    public long getDurationInMonths() {
        return dateInterval.getMonthLength();
    }

    public boolean containsDate(LocalDate date) {
        return dateInterval.contains(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcademicTerm that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}