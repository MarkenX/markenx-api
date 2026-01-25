package com.udla.markenx.api.game.attempts.infrastructure.web.rest;

import com.udla.markenx.api.game.attempts.application.ports.in.commands.RegisterGameSessionCommand;
import com.udla.markenx.api.game.attempts.application.ports.in.queries.GetAttemptByIdQuery;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.AttemptQueryUseCase;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.RegisterGameSessionUseCase;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.AttemptMetricsResponseDTO;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.GameSessionResponseDTO;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.RegisterGameSessionRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("attempts")
@RequiredArgsConstructor
@Tag(name = "Attempts", description = "Game session results management")
public class AttemptController {

    private final RegisterGameSessionUseCase registerGameSessionUseCase;
    private final AttemptQueryUseCase attemptQueryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a game session result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game session registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task or student not found"),
            @ApiResponse(responseCode = "409", description = "Conflict with existing attempt"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public GameSessionResponseDTO registerGameSession(@RequestBody @Valid RegisterGameSessionRequestDTO dto) {
        var command = RegisterGameSessionCommand.from(dto);
        var response = registerGameSessionUseCase.handle(command);
        return GameSessionResponseDTO.from(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a game session result by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game session retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid id format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Attempt not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public GameSessionResponseDTO getById(@PathVariable String id) {
        var query = new GetAttemptByIdQuery(id);
        var response = attemptQueryUseCase.getById(query);
        return GameSessionResponseDTO.from(response);
    }

    @GetMapping("/{attemptId}/metrics")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get performance metrics for an attempt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid id format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Attempt not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AttemptMetricsResponseDTO getMetrics(@PathVariable String attemptId) {
        var query = new GetAttemptByIdQuery(attemptId);
        var response = attemptQueryUseCase.getById(query);
        return AttemptMetricsResponseDTO.from(response);
    }
}
