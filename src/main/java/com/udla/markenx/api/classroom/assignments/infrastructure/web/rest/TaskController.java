package com.udla.markenx.api.classroom.assignments.infrastructure.web.rest;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.CreateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.QueryTasksUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskPageQueryCriteria;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskIdQuery;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos.CreateTaskRequestDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos.TaskAttemptResponseDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos.TaskResponseDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.mappers.TaskDTOMapper;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.AttemptQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final QueryTasksUseCase queryTasksUseCase;
    private final AttemptQueryUseCase attemptQueryUseCase;

    private final TaskDTOMapper mapper = new TaskDTOMapper();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Task already exists or conflict"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public TaskResponseDTO create(@RequestBody @Valid CreateTaskRequestDTO request) {
        return mapper.toResponseDTO(
                createTaskUseCase.handle(CreateTaskCommand.from(request))
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No task found")
    })
    public TaskResponseDTO getById(@PathVariable String id) {
        var query = new TaskIdQuery(id);
        return mapper.toResponseDTO(queryTasksUseCase.getTaskById(query));
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
        var query = new TaskPageQueryCriteria(page, size);
        Page<@NotNull TaskResponseDTO> result =
                queryTasksUseCase.listTasksPage(query).map(mapper::toResponseDTO);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{taskId}/attempts")
    @Operation(summary = "Get all attempts for a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attempts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No attempts found for task")
    })
    public ResponseEntity<List<TaskAttemptResponseDTO>> getAttemptsByTaskId(@PathVariable String taskId) {
        var attempts = attemptQueryUseCase.getByTaskId(taskId);
        if (attempts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attempts.stream()
                .map(attempt -> new TaskAttemptResponseDTO(
                        attempt.attemptId(),
                        attempt.taskId(),
                        attempt.evaluatedAt(),
                        attempt.evaluatedAt(), // finishedAt - using sessionDate as placeholder
                        attempt.finalOutcome(),
                        attempt.finalOutcome(),
                        attempt.finalAcceptance()
                ))
                .toList());
    }
}
