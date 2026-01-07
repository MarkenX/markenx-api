package com.udla.markenx.api.game.scenarios.domain.ports.outgoing;

import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ScenarioQueryRepository {
    List<Scenario> findAll();
    Optional<Scenario> findById(String id);
    Page<Scenario> findAllPaginated(Pageable pageable);
    List<String> findDimensionIdsByScenarioId(String scenarioId);
    List<String> findActionIdsByScenarioId(String scenarioId);
    List<String> findEventIdsByScenarioId(String scenarioId);
}
