package com.udla.markenx.api.game.scenarios.domain.ports.outgoing;

import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScenarioQueryRepository {
    List<Scenario> findAll();
    Page<@NotNull Scenario> findAllPaginated(Pageable pageable);
}
