package com.udla.markenx.api.game.scenarios.infrastructure.seeders;

import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand;
import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand.ActionDTO;
import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand.ActionEffectDTO;
import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand.ConsumerDTO;
import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand.DimensionDTO;
import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand.EventDTO;
import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand.EventEffectDTO;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioResponse;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.CreateScenarioUseCase;
import com.udla.markenx.api.game.scenarios.domain.exceptions.ScenarioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Profile("dev")
@Order(5)
@RequiredArgsConstructor
public class ScenarioSeeder implements CommandLineRunner {

    private final CreateScenarioUseCase createScenarioUseCase;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding scenarios...");

        try {
            CreateScenarioCommand command = buildSampleScenarioCommand();
            ScenarioResponse saved = createScenarioUseCase.handle(command);
            log.info("The scenario '{}' was created with id: {}", saved.title(), saved.id());
            log.info("Scenarios seeded successfully.");
        } catch (ScenarioException e) {
            log.error(e.getMessage(), e);
            log.info("Scenarios seeding failed.");
        }
    }

    private CreateScenarioCommand buildSampleScenarioCommand() {
        String priceId = UUID.randomUUID().toString();
        String qualityId = UUID.randomUUID().toString();
        String brandId = UUID.randomUUID().toString();

        List<DimensionDTO> dimensions = buildDimensions(priceId, qualityId, brandId);
        ConsumerDTO consumer = buildConsumer();
        List<ActionDTO> actions = buildActions(priceId, qualityId, brandId);
        List<EventDTO> events = buildEvents(priceId, qualityId);

        return new CreateScenarioCommand(
                "Escenario de Lanzamiento de Producto",
                "Simulación de estrategia de marketing para el lanzamiento de un nuevo producto tecnológico",
                consumer,
                dimensions,
                actions,
                events
        );
    }

    private List<DimensionDTO> buildDimensions(String priceId, String qualityId, String brandId) {
        return List.of(
                new DimensionDTO(
                        priceId,
                        "price",
                        "Precio",
                        "Percepción del precio del producto por parte del consumidor",
                        0.7,
                        0.5
                ),
                new DimensionDTO(
                        qualityId,
                        "quality",
                        "Calidad",
                        "Percepción de la calidad del producto",
                        0.8,
                        0.6
                ),
                new DimensionDTO(
                        brandId,
                        "brand_image",
                        "Imagen de Marca",
                        "Percepción de la imagen y reputación de la marca",
                        0.6,
                        0.4
                )
        );
    }

    @Contract(" -> new")
    private @NonNull ConsumerDTO buildConsumer() {
        return new ConsumerDTO(
                UUID.randomUUID().toString(),
                "Consumidor Tecnológico",
                28,
                new BigDecimal("1500.00"),
                0.75
        );
    }

    private List<ActionDTO> buildActions(String priceId, String qualityId, String brandId) {
        String discountActionId = UUID.randomUUID().toString();
        String qualityActionId = UUID.randomUUID().toString();
        String brandActionId = UUID.randomUUID().toString();
        String premiumActionId = UUID.randomUUID().toString();

        return List.of(
                new ActionDTO(
                        discountActionId,
                        "apply_discount",
                        "Aplicar descuento del 15% al producto",
                        new BigDecimal("500.00"),
                        "PRICING",
                        false,
                        null,
                        List.of(new ActionEffectDTO(priceId, 0.2))
                ),
                new ActionDTO(
                        qualityActionId,
                        "improve_quality",
                        "Mejorar los materiales y componentes del producto",
                        new BigDecimal("1200.00"),
                        "PRODUCT",
                        false,
                        null,
                        List.of(
                                new ActionEffectDTO(qualityId, 0.3),
                                new ActionEffectDTO(priceId, -0.1)
                        )
                ),
                new ActionDTO(
                        brandActionId,
                        "marketing_campaign",
                        "Lanzar campaña de marketing en redes sociales",
                        new BigDecimal("800.00"),
                        "MARKETING",
                        false,
                        null,
                        List.of(new ActionEffectDTO(brandId, 0.25))
                ),
                new ActionDTO(
                        premiumActionId,
                        "premium_package",
                        "Crear versión premium con garantía extendida",
                        new BigDecimal("300.00"),
                        "PRODUCT",
                        true,
                        qualityActionId,
                        List.of(
                                new ActionEffectDTO(qualityId, 0.15),
                                new ActionEffectDTO(brandId, 0.1)
                        )
                )
        );
    }

    private List<EventDTO> buildEvents(String priceId, String qualityId) {
        return List.of(
                new EventDTO(
                        UUID.randomUUID().toString(),
                        "Competidor lanza producto similar",
                        "Un competidor importante ha lanzado un producto con características similares a menor precio",
                        List.of(
                                new EventEffectDTO(priceId, 1.5),
                                new EventEffectDTO(qualityId, 0.8)
                        )
                ),
                new EventDTO(
                        UUID.randomUUID().toString(),
                        "Reseña viral positiva",
                        "Un influencer tecnológico publica una reseña muy positiva del producto",
                        List.of(new EventEffectDTO(qualityId, 1.3))
                )
        );
    }
}
