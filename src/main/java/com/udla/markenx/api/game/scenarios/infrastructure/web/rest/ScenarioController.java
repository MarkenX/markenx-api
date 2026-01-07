package com.udla.markenx.api.game.scenarios.infrastructure.web.rest;

import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioDetailResponse;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioResponse;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioSummaryResponse;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.CreateScenarioUseCase;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.ScenarioQueryUseCase;
import com.udla.markenx.api.game.scenarios.application.queries.GetAllScenariosPaginatedQuery;
import com.udla.markenx.api.game.scenarios.application.queries.GetScenarioByIdQuery;
import com.udla.markenx.api.game.scenarios.infrastructure.web.rest.dtos.CreateScenarioRequestDTO;
import com.udla.markenx.api.game.scenarios.infrastructure.web.rest.mappers.ScenarioRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("scenarios")
@Tag(name = "Scenarios", description = "Gestión de escenarios del videojuego")
public class ScenarioController {

    private final CreateScenarioUseCase createScenarioUseCase;
    private final ScenarioQueryUseCase scenarioQueryUseCase;
    private final ScenarioRequestMapper requestMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un nuevo escenario con configuración completa")
    public ScenarioResponse create(@RequestBody CreateScenarioRequestDTO dto) {
        var command = requestMapper.toCommand(dto);
        return createScenarioUseCase.handle(command);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener escenario por ID con todas las relaciones")
    public ResponseEntity<ScenarioDetailResponse> getById(@PathVariable String id) {
        var response = scenarioQueryUseCase.getById(new GetScenarioByIdQuery(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los escenarios paginados")
    public ResponseEntity<Page<ScenarioSummaryResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var response = scenarioQueryUseCase.getAllPaginated(
                new GetAllScenariosPaginatedQuery(page, size)
        );

        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
