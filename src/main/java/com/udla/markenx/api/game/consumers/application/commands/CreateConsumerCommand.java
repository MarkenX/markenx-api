package com.udla.markenx.api.game.consumers.application.commands;

import java.math.BigDecimal;

public record CreateConsumerCommand(
        String name,
        Integer age,
        BigDecimal budget,
        double targetAcceptanceScore
) {
}
