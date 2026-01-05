package com.udla.markenx.api.classroom.academicterms.domain.utils;

import com.udla.markenx.api.classroom.academicterms.domain.exceptions.InsufficientMonthsAfterYearStartException;
import com.udla.markenx.api.classroom.academicterms.domain.exceptions.InsufficientMonthsBeforeYearEndException;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.DateInterval;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    private DateUtils() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no debe ser instanciada");
    }

    public static long monthsToEndOfYear(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        LocalDate endOfYear = LocalDate.of(date.getYear(), 12, 31);
        return ChronoUnit.MONTHS.between(
                date.withDayOfMonth(1),
                endOfYear.withDayOfMonth(1)
        );
    }

    public static long monthsFromStartOfYear(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        LocalDate startOfYear = LocalDate.of(date.getYear(), 1, 1);
        return ChronoUnit.MONTHS.between(
                startOfYear.withDayOfMonth(1),
                date.withDayOfMonth(1)
        );
    }

    public static void validateCrossYearIntervalMonths(
            DateInterval interval,
            int minMonthsPerYear
    ) {
        if (interval == null) {
            throw new IllegalArgumentException("El intervalo no puede ser nulo");
        }

        if (minMonthsPerYear < 0) {
            throw new IllegalArgumentException("Los meses mínimos por año deben ser un valor positivo");
        }

        long monthsToEnd = monthsToEndOfYear(interval.getStartDate());
        long monthsFromStart = monthsFromStartOfYear(interval.getEndDate());

        if (monthsToEnd < minMonthsPerYear) {
            throw new InsufficientMonthsBeforeYearEndException(monthsToEnd, minMonthsPerYear);
        }

        if (monthsFromStart < minMonthsPerYear) {
            throw new InsufficientMonthsAfterYearStartException(monthsFromStart, minMonthsPerYear);
        }
    }

    public static boolean isInYear(LocalDate date, int year) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        return date.getYear() == year;
    }

    public static long monthsBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }

        return ChronoUnit.MONTHS.between(
                startDate.withDayOfMonth(1),
                endDate.withDayOfMonth(1)
        );
    }
}