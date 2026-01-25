package com.udla.markenx.api.game.scenarios.infrastructure.adapters;

import com.udla.markenx.api.classroom.assignments.application.ports.out.ScenarioValidationPort;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.ValidateScenarioUseCase;
import com.udla.markenx.api.game.scenarios.application.queries.ScenarioExistsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adapter that implements the ScenarioValidationPort from the assignments module.
 * This acts as an Anti-Corruption Layer, exposing only the validation logic
 * that the assignments module needs without coupling to internal game types.
 */
@Component
@RequiredArgsConstructor
public class ScenarioValidationAdapter implements ScenarioValidationPort {

    private final ValidateScenarioUseCase validateScenarioUseCase;

    @Override
    public boolean existsById(String scenarioId) {
        return validateScenarioUseCase.exists(new ScenarioExistsQuery(scenarioId));
    }
}
