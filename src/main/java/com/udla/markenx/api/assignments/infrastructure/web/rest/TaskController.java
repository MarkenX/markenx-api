package com.udla.markenx.api.assignments.infrastructure.web.rest;

import com.udla.markenx.api.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.assignments.application.ports.incoming.SaveTaskUseCase;
import com.udla.markenx.api.assignments.application.ports.incoming.TaskQueryUseCase;
import com.udla.markenx.api.assignments.application.queries.GetAllTasksPaginatedQuery;
import com.udla.markenx.api.assignments.infrastructure.web.rest.dtos.CreateTaskRequestDTO;
import com.udla.markenx.api.assignments.infrastructure.web.rest.dtos.TaskResponseDTO;
import com.udla.markenx.api.assignments.infrastructure.web.rest.mappers.TaskResponseDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("tasks")
public class TaskController {

    private final TaskResponseDTOMapper mapper;
    private final SaveTaskUseCase saveTaskUseCase;
    private final TaskQueryUseCase taskQueryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully")
    })
    public TaskResponseDTO create(@RequestBody CreateTaskRequestDTO dto) {
        var command = new SaveTaskCommand(
                dto.title(),
                dto.summary(),
                dto.deadline(),
                dto.minScoreToPass(),
                dto.courseId(),
                dto.maxAttempts(),
                false
        );
        return mapper.toDTO(saveTaskUseCase.handle(command));
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No tasks found")
    })
    public ResponseEntity<@NotNull Page<@NotNull TaskResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var query = new GetAllTasksPaginatedQuery(page, size);
        Page<@NotNull TaskResponseDTO> result =
                taskQueryUseCase.getAllPaginated(query).map(mapper::toDTO);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}
