package com.udla.markenx.api.classroom.terms.domain.models.aggregates;

import com.udla.markenx.api.classroom.terms.domain.exceptions.EndDateCannotBeNullException;
import com.udla.markenx.api.classroom.terms.domain.exceptions.InvalidDateIntervalException;
import com.udla.markenx.api.classroom.terms.domain.exceptions.StartDateCannotBeNullException;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

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

    /**
     * Retorna la duración exacta en meses, redondeando hacia arriba si hay días parciales.
     */
    public long getMonthLength() {
        long months = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), endDate.withDayOfMonth(1));
        // Si hay días adicionales, contamos como un mes más
        if (endDate.getDayOfMonth() > startDate.getDayOfMonth()) {
            months++;
        }
        return months;
    }

    /**
     * Verifica si dos intervalos se superponen.
     * Interpreta intervalos como [startDate, endDate] cerrados.
     */
    public boolean overlapsWith(@NonNull DateInterval other) {
        // Dos intervalos se superponen si hay al menos un día común
        return !this.endDate.isBefore(other.startDate) && !this.startDate.isAfter(other.endDate);
    }

    /**
     * Verifica si la fecha está dentro del intervalo [startDate, endDate].
     */
    public boolean contains(@NotNull LocalDate date) {
        return !(date.isBefore(startDate) || date.isAfter(endDate));
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
