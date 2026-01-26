package com.udla.markenx.api.classroom.assignments.infrastructure.seeders.factories;

import com.udla.markenx.api.classroom.assignments.infrastructure.seeders.valueobjects.TaskSeedDefinition;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public final class TaskSeedFactory {

    private TaskSeedFactory() {}

    @Contract("_ -> new")
    public static @NonNull List<TaskSeedDefinition> forActiveCourse(LocalDateTime now) {
        return List.of(
                productLaunchSimulation(now),
                pricingStrategy(now),
                digitalMarketingCampaign(now),
                historicalOutdatedTask(now)
        );
    }

    @Contract("_ -> new")
    private static @NonNull TaskSeedDefinition productLaunchSimulation(LocalDateTime now) {
        return new TaskSeedDefinition(
                "Simulación de lanzamiento de producto",
                """
                Simula el lanzamiento de un nuevo producto al mercado.
                Incluye análisis de público objetivo, propuesta de valor
                y métricas de aceptación esperadas.
                """,
                now.plusWeeks(4),
                0.70,
                3,
                false
        );
    }

    @Contract("_ -> new")
    private static @NonNull TaskSeedDefinition pricingStrategy(LocalDateTime now) {
        return new TaskSeedDefinition(
                "Estrategia de precios",
                """
                Diseña una estrategia de pricing basada en costos,
                competencia y percepción de valor del cliente.
                Justifica la estrategia elegida.
                """,
                now.plusWeeks(6),
                0.65,
                3,
                false
        );
    }

    @Contract("_ -> new")
    private static @NonNull TaskSeedDefinition digitalMarketingCampaign(LocalDateTime now) {
        return new TaskSeedDefinition(
                "Campaña de marketing digital",
                """
                Diseña y ejecuta una campaña de marketing digital.
                Define canales, presupuesto, KPIs y cronograma.
                """,
                now.plusWeeks(10),
                0.75,
                2,
                false
        );
    }

    @Contract("_ -> new")
    private static @NonNull TaskSeedDefinition historicalOutdatedTask(LocalDateTime now) {
        return new TaskSeedDefinition(
                "Análisis post-lanzamiento (histórico)",
                """
                Analiza los resultados obtenidos después del lanzamiento
                del producto y documenta aprendizajes clave.
                """,
                now.minusWeeks(2),
                0.80,
                5,
                true
        );
    }
}
