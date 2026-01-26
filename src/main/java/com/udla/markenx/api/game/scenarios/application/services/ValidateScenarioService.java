package com.udla.markenx.api.game.scenarios.application.services;

import com.udla.markenx.api.game.scenarios.application.ports.incoming.ValidateScenarioUseCase;
import com.udla.markenx.api.game.scenarios.application.queries.ScenarioExistsQuery;
import com.udla.markenx.api.game.scenarios.domain.ports.outgoing.ScenarioQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateScenarioService implements ValidateScenarioUseCase {

    private final ScenarioQueryRepository scenarioRepository;

    @Override
    public boolean exists(@NonNull ScenarioExistsQuery query) {
        return scenarioRepository.findById(query.scenarioId()).isPresent();
    }
}
