package com.udla.markenx.api.game.scenarios.infrastructure.web.rest;

import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioDetailResponse;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioResponse;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioSummaryResponse;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.CreateScenarioUseCase;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.ScenarioQueryUseCase;
import com.udla.markenx.api.game.scenarios.application.queries.GetAllScenariosPaginatedQuery;
import com.udla.markenx.api.game.scenarios.application.queries.GetScenarioByIdQuery;
import com.udla.markenx.api.game.scenarios.infrastructure.web.rest.dtos.CreateScenarioRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("scenarios")
@Tag(name = "Scenarios", description = "Gestión de escenarios del videojuego")
public class ScenarioController {

    private final CreateScenarioUseCase createScenarioUseCase;
    private final ScenarioQueryUseCase scenarioQueryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new scenario with complete configuration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Scenario created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Scenario already exists or conflict"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ScenarioResponse create(@RequestBody @Valid CreateScenarioRequestDTO dto) {
        var command = CreateScenarioCommand.from(dto);
        return createScenarioUseCase.handle(command);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get scenario by ID with all relations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scenario retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid id format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Scenario not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ScenarioDetailResponse getById(@PathVariable String id) {
        return scenarioQueryUseCase.getById(new GetScenarioByIdQuery(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all scenarios (paged)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scenarios retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Page<ScenarioSummaryResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return scenarioQueryUseCase.getAllPaginated(
                new GetAllScenariosPaginatedQuery(page, size)
        );
    }
}
