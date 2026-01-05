package com.udla.markenx.api.classroom.assignments.domain.models.valueobjects;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.DueDateMustBeInTheFutureException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.DueDateNotProvidedException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AssignmentDeadline(LocalDateTime value) {

    public static @NonNull AssignmentDeadline future(LocalDateTime value) {
        return new AssignmentDeadline(validateFuture(value, LocalDateTime.now()));
    }

    @Contract("null -> fail; !null -> new")
    public static @NonNull AssignmentDeadline historical(LocalDateTime value) {
        if (value == null)
            throw new DueDateNotProvidedException();
        return new AssignmentDeadline(value);
    }

    @Contract("_, _ -> param1")
    private static @NonNull LocalDateTime validateFuture(@NonNull LocalDateTime value, LocalDateTime now) {
        if (!value.isAfter(now))
            throw new DueDateMustBeInTheFutureException();
        return value;
    }

    public LocalDate date() {
        return value.toLocalDate();
    }

    public LocalTime time() {
        return value.toLocalTime();
    }

    public boolean isOverdue() {
        return value.isBefore(LocalDateTime.now());
    }
}
