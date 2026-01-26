package com.udla.markenx.api.game.attempts.infrastructure.seeders.factories;

import com.udla.markenx.api.game.attempts.application.ports.in.commands.RegisterGameSessionCommand.TurnHistoryDTO;
import com.udla.markenx.api.game.attempts.infrastructure.seeders.valueobjects.AttemptSeedDefinition;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AttemptSeedFactory {

    private AttemptSeedFactory() {}

    @Contract("_, _ -> new")
    public static @NonNull AttemptSeedDefinition successful(@NonNull LocalDateTime now, int variation) {
        return build(
                now.minusDays(2 + variation),
                0.78 + variation * 0.03,
                0.82 + variation * 0.02,
                5 + variation,
                new BigDecimal("220.00").subtract(BigDecimal.valueOf(variation * 25L))
        );
    }

    @Contract("_, _ -> new")
    public static @NonNull AttemptSeedDefinition failed(@NonNull LocalDateTime now, int variation) {
        return build(
                now.minusDays(3 + variation),
                0.55 + variation * 0.02,
                0.50 + variation * 0.02,
                4 + variation,
                new BigDecimal("150.00").subtract(BigDecimal.valueOf(variation * 20L))
        );
    }

    @Contract("_, _, _, _, _ -> new")
    private static @NonNull AttemptSeedDefinition build(
            LocalDateTime startedAt,
            double finalAcceptance,
            double profileScore,
            int turnsUsed,
            BigDecimal finalBudget
    ) {
        return new AttemptSeedDefinition(
                startedAt,
                round(finalAcceptance),
                finalBudget,
                turnsUsed,
                round(profileScore),
                buildTurnHistory(turnsUsed, finalAcceptance, finalBudget)
        );
    }

    private static @NonNull List<TurnHistoryDTO> buildTurnHistory(
            int totalTurns,
            double finalAcceptance,
            BigDecimal finalBudget
    ) {
        List<TurnHistoryDTO> history = new ArrayList<>();

        double startAcceptance = 0.30;
        BigDecimal startBudget = new BigDecimal("500.00");

        for (int turn = 1; turn <= totalTurns; turn++) {
            double progress = (double) turn / totalTurns;

            double acceptance = startAcceptance +
                    (finalAcceptance - startAcceptance) * progress;

            BigDecimal budget = startBudget.subtract(
                    startBudget.subtract(finalBudget)
                            .multiply(BigDecimal.valueOf(progress))
            );

            history.add(new TurnHistoryDTO(
                    turn,
                    round(acceptance),
                    budget.setScale(2, RoundingMode.HALF_UP),
                    turn == 3 ? "Tendencia Viral" : null,
                    Collections.emptyList()
            ));
        }

        return history;
    }

    private static double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}

