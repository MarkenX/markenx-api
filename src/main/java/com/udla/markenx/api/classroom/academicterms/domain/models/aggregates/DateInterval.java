package com.udla.markenx.api.classroom.academicterms.domain.models.aggregates;

import com.udla.markenx.api.classroom.academicterms.domain.exceptions.EndDateCannotBeNullException;
import com.udla.markenx.api.classroom.academicterms.domain.exceptions.InvalidDateIntervalException;
import com.udla.markenx.api.classroom.academicterms.domain.exceptions.StartDateCannotBeNullException;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Getter
public class DateInterval {

    private LocalDate startDate;
    private LocalDate endDate;

    public DateInterval(LocalDate startDate, LocalDate endDate) {
        this.startDate = validateStartDate(startDate);
        this.endDate = validateEndDate(endDate);
        validateInterval(this.startDate, this.endDate);
    }

    public void setStartDate(LocalDate startDate) {
        validateInterval(startDate, this.endDate);
        this.startDate = validateStartDate(startDate);
    }

    public void setEndDate(LocalDate endDate) {
        validateInterval(this.startDate, endDate);
        this.endDate = validateEndDate(endDate);
    }

    @Contract("null -> fail; !null -> param1")
    private @NotNull LocalDate validateStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new StartDateCannotBeNullException();
        }
        return startDate;
    }

    @Contract("null -> fail; !null -> param1")
    private @NotNull LocalDate validateEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new EndDateCannotBeNullException();
        }
        return endDate;
    }

    private void validateInterval(LocalDate startDate, @NotNull LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidDateIntervalException(startDate, endDate);
        }
    }

    public int getEndYear() {
        return endDate.getYear();
    }

    public boolean spansOneYear() {
        return startDate.getYear() == endDate.getYear();
    }

    public boolean spansMultipleYears() {
        return !spansOneYear();
    }

    public long getMonthLength() {
        return ChronoUnit.MONTHS.between(startDate, endDate);
    }

    public boolean overlapsWith(DateInterval other) {
        return this.equals(other)
                || contains(other.startDate)
                || contains(other.endDate);
    }

    public boolean contains(@NotNull LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate))
                && (date.isEqual(endDate) || date.isBefore(endDate));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateInterval that)) return false;
        return Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}
