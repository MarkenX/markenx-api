package com.udla.markenx.api.game.consumers.application.commands;

import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

public record CreateConsumerCommand(
        @Nullable String id,
        String name,
        Integer age,
        BigDecimal budget,
        double targetAcceptanceScore
) {
    public CreateConsumerCommand(
            String name,
            Integer age,
            BigDecimal budget,
            double targetAcceptanceScore
    ) {
        this(null, name, age, budget, targetAcceptanceScore);
    }
}
