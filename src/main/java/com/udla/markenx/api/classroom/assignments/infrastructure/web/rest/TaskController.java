package com.udla.markenx.api.classroom.assignments.infrastructure.web.rest;

import com.udla.markenx.api.classroom.academicterms.application.dtos.AcademicTermResponseDTO;
import com.udla.markenx.api.classroom.academicterms.application.queries.GetAcademicTermByIdQuery;
import com.udla.markenx.api.classroom.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.SaveTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.TaskQueryUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.UpdateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.queries.GetAllTasksPaginatedQuery;
import com.udla.markenx.api.classroom.assignments.application.queries.GetTaskByIdQuery;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos.CreateTaskRequestDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos.TaskAttemptResponseDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos.TaskResponseDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.mappers.TaskResponseDTOMapper;
import com.udla.markenx.api.game.attempts.application.ports.incoming.AttemptQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private final TaskResponseDTOMapper mapper;
    private final SaveTaskUseCase saveTaskUseCase;
    private final TaskQueryUseCase taskQueryUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final AttemptQueryUseCase attemptQueryUseCase;

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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No task found")
    })
    public TaskResponseDTO getById(@PathVariable String id) {
        var query = new GetTaskByIdQuery(id);
        return mapper.toDTO(updateTaskUseCase.getById(query));
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
                        attempt.getId(),
                        attempt.getTaskId(),
                        attempt.getSessionDate(),
                        attempt.getSessionDate(), // finishedAt - using sessionDate as placeholder
                        attempt.getStatus().name(),
                        attempt.getStatus().name().equals("FINISHED") ? "WIN" : "IN_PROGRESS",
                        attempt.getResult().profileScore()
                ))
                .toList());
    }
}
