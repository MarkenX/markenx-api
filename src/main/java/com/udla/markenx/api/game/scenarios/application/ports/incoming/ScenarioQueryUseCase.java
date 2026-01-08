package com.udla.markenx.api.game.scenarios.application.ports.incoming;

import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioDetailResponse;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioSummaryResponse;
import com.udla.markenx.api.game.scenarios.application.queries.GetAllScenariosPaginatedQuery;
import com.udla.markenx.api.game.scenarios.application.queries.GetScenarioByIdQuery;
import org.springframework.data.domain.Page;

public interface ScenarioQueryUseCase {
    ScenarioDetailResponse getById(GetScenarioByIdQuery query);
    Page<ScenarioSummaryResponse> getAllPaginated(GetAllScenariosPaginatedQuery query);
}
