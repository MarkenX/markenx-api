package com.udla.markenx.api.classroom.terms.infrastructure.seeders.factories;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.CreateTermCommand;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class TermSeedFactory {

    // =====================
    // Definición de periodos
    // =====================
    private static final LocalDate PAST_START    = LocalDate.of(2024, 12, 1);
    private static final LocalDate PAST_END      = LocalDate.of(2025, 4, 1);  // 4 meses

    private static final LocalDate CURRENT_START = LocalDate.of(2025, 9, 1);
    private static final LocalDate CURRENT_END   = LocalDate.of(2026, 2, 1);  // 5 meses

    private static final LocalDate FUTURE_START  = LocalDate.of(2026, 3, 1);
    private static final LocalDate FUTURE_END    = LocalDate.of(2026, 7, 1);  // 4 meses

    // =====================
    // Métodos públicos
    // =====================
    @Contract("-> new")
    public static @NonNull CreateTermCommand past() {
        return new CreateTermCommand(
                PAST_START,
                PAST_END,
                2024,
                true
        );
    }

    @Contract("-> new")
    public static @NonNull CreateTermCommand current() {
        return new CreateTermCommand(
                CURRENT_START,
                CURRENT_END,
                2025,
                true
        );
    }

    @Contract("-> new")
    public static @NonNull CreateTermCommand future() {
        return new CreateTermCommand(
                FUTURE_START,
                FUTURE_END,
                2025,
                false
        );
    }
}
