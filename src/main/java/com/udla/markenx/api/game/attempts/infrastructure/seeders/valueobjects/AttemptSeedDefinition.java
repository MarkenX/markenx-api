package com.udla.markenx.api.game.attempts.infrastructure.seeders.valueobjects;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.udla.markenx.api.game.attempts.application.ports.in.commands.RegisterGameSessionCommand.TurnHistoryDTO;

public record AttemptSeedDefinition(
        LocalDateTime startedAt,
        double finalAcceptance,
        BigDecimal remainingBudget,
        int turnsUsed,
        double profileScore,
        List<TurnHistoryDTO> history
) {}
