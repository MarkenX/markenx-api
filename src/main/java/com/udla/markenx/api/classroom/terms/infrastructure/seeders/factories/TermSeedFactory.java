package com.udla.markenx.api.classroom.terms.infrastructure.seeders.factories;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.CreateTermCommand;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class TermSeedFactory {

    @Contract("_ -> new")
    public static @NonNull CreateTermCommand past(@NonNull LocalDate today) {
        return new CreateTermCommand(
                today.minusMonths(10),
                today.minusMonths(6),
                today.minusYears(1).getYear(),
                true
        );
    }

    @Contract("_ -> new")
    public static @NonNull CreateTermCommand current(@NonNull LocalDate today) {
        return new CreateTermCommand(
                today.minusMonths(1),
                today.plusMonths(4),
                today.getYear(),
                true
        );
    }

    @Contract("_ -> new")
    public static @NonNull CreateTermCommand future(@NonNull LocalDate today) {
        return new CreateTermCommand(
                today.plusMonths(5),
                today.plusMonths(9),
                today.getYear(),
                false
        );
    }
}