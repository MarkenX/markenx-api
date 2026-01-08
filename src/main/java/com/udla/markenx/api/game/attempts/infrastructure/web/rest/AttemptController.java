package com.udla.markenx.api.game.attempts.infrastructure.web.rest;

import com.udla.markenx.api.game.attempts.application.commands.GetAttemptByIdQuery;
import com.udla.markenx.api.game.attempts.application.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.application.ports.incoming.AttemptQueryUseCase;
import com.udla.markenx.api.game.attempts.application.ports.incoming.RegisterGameSessionUseCase;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.GameSessionResponseDTO;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.RegisterGameSessionRequestDTO;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.mappers.AttemptRequestMapper;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.mappers.AttemptResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("attempts")
@RequiredArgsConstructor
@Tag(name = "Attempts", description = "Game session results management")
public class AttemptController {

    private final RegisterGameSessionUseCase registerGameSessionUseCase;
    private final AttemptQueryUseCase attemptQueryUseCase;
    private final AttemptRequestMapper requestMapper;
    private final AttemptResponseMapper responseMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a game session result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game session registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public GameSessionResponseDTO registerGameSession(@RequestBody RegisterGameSessionRequestDTO dto) {
        var command = requestMapper.toCommand(dto);
        GameSessionResponse response = registerGameSessionUseCase.handle(command);
        return responseMapper.toDTO(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a game session result by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game session retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Attempt not found")
    })
    public ResponseEntity<GameSessionResponseDTO> getById(@PathVariable String id) {
        var query = new GetAttemptByIdQuery(id);
        GameSessionResponse response = attemptQueryUseCase.getById(query);
        return ResponseEntity.ok(responseMapper.toDTO(response));
    }
}
