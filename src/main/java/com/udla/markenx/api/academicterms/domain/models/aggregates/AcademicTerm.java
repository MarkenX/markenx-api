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

    // region Constants

    private static final int YEAR_HISTORICAL_THRESHOLD = 2024;
    private static final int MAX_YEARS_IN_FUTURE = 1;
    private static final int MIN_MONTHS_LENGTH = 4;
    private static final int MAX_MONTHS_LENGTH = 6;
    private static final int MIN_MONTHS_PER_YEAR = 2;

    // endregion

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

    // region Getters

    public LocalDate getStartDate() {
        return this.dateInterval.startDate();
    }

    public LocalDate getEndDate() {
        return this.dateInterval.endDate();
    }

    // endregion

    //region Validations

    /**
     * Validates whether the provided academic year is within the acceptable range.
     * The year must be greater than or equal to the historical threshold and not exceed the next calendar year.
     *
     * @param year the academic year to validate
     * @return the validated academic year if it is valid
     * @throws InvalidAcademicYearException if the year is less than the historical threshold
     *                                      or greater than the next calendar year
     */
    private int validateYear(int year) {
        var nextYear = LocalDate.now().getYear() + 1;
        if (year < YEAR_HISTORICAL_THRESHOLD || year > nextYear) {
            throw new InvalidAcademicYearException(year, YEAR_HISTORICAL_THRESHOLD, nextYear);
        }
        return year;
    }

    /**
     * Validates whether the provided term sequence is within the acceptable range.
     * The sequence must be a positive number greater than the minimum required
     * and not exceeding the maximum defined value.
     *
     * @param sequence the term sequence to validate
     * @return the validated term sequence if it is valid
     * @throws InvalidTermSequenceException if the sequence is less than or equal to zero,
     *                                      or if it is outside the defined minimum and maximum limits
     */
    private int validateSequence(int sequence) {
        int minSequence = Math.ceilDiv(12, MIN_MONTHS_LENGTH);
        int maxSequence = Math.ceilDiv(12, MAX_MONTHS_LENGTH);
        if (sequence <= 0 || sequence < minSequence || sequence > maxSequence) {
            throw new InvalidTermSequenceException(sequence);
        }
        return sequence;
    }

    private void validateCommonInvariants(DateInterval dateInterval) {
        validateStartDateInFuture(dateInterval);
        validateEndDateNotTooFar(dateInterval);
        validateMonthLength(dateInterval);
    }

    /**
     * Validates whether the provided date interval is confined within a single calendar year.
     * Throws an exception if the interval spans multiple years.
     *
     * @param interval the date interval to validate, must not be null
     * @throws TermMustBeWithinSingleYearException if the date interval spans multiple calendar years
     */
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

    /**
     * Validates that the start date of the provided date interval is in the future.
     * If the start date is not in the future, an exception is thrown.
     *
     * @param interval the date interval to validate, must not be null
     * @throws TermMustStartInFutureException if the start date is not in the future
     */
    private void validateStartDateInFuture(@NotNull DateInterval interval) {
        if (!LocalDate.now().isBefore(interval.startDate())) {
            throw new TermMustStartInFutureException(interval.startDate());
        }
    }


    /**
     * Validates that the end year of the provided date interval does not exceed the maximum allowable year.
     * If the end year is beyond the acceptable range, an exception is thrown.
     *
     * @param interval the date interval to validate, must not be null
     * @throws TermCannotBeCreatedTooFarInFutureException if the end year of the interval exceeds the maximum allowable year
     */
    private void validateEndDateNotTooFar(@NotNull DateInterval interval) {
        var maxAllowedYear = LocalDate.now().getYear() + MAX_YEARS_IN_FUTURE;
        if (interval.getEndYear() > maxAllowedYear) {
            throw new TermCannotBeCreatedTooFarInFutureException(
                    interval.getEndYear(),
                    maxAllowedYear
            );
        }
    }

    /**
     * Validates whether the length of the month interval is within the accepted range.
     * If the month length is less than the defined minimum or greater than the defined maximum,
     * an exception is thrown.
     *
     * @param interval the date interval to validate; must not be null
     * @throws InvalidTermLengthException if the month length of the interval is less than the minimum allowed
     *                                    or greater than the maximum allowed
     */
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
     * @throws InsufficientMonthsBeforeYearEndException  if the number of months from the start date to the end of the year is less than the minimum required
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

    //endregion

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