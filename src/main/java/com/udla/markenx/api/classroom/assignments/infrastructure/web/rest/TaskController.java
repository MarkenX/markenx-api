package com.udla.markenx.api.classroom.assignments.infrastructure.web.rest;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.ChangeTaskStatusCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.CreateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.QueryTasksUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskPageQueryCriteria;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskIdQuery;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.UpdateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskAttemptQueryPort;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.requests.CreateTaskRequestDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.requests.UpdateTaskRequestDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.requests.UpdateTaskStatusRequestDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.responses.CreateTaskResponseDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.responses.TaskAttemptResponseDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.responses.TaskResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final QueryTasksUseCase queryTasksUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final TaskAttemptQueryPort taskAttemptQueryPort;

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
    public CreateTaskResponseDTO create(@RequestBody @Valid CreateTaskRequestDTO request) {
        var newTask = createTaskUseCase.handle(CreateTaskCommand.from(request));
        return CreateTaskResponseDTO.from(newTask);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a task by attemptId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid attemptId format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    public TaskResponseDTO getById(@PathVariable String id) {
        var task = queryTasksUseCase.getTaskById(TaskIdQuery.from(id));
        return TaskResponseDTO.from(task);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or malformed JSON"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "409", description = "Invalid status change or conflict"),
            @ApiResponse(responseCode = "422", description = "Domain rule violation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public TaskResponseDTO changeStatus(
            @PathVariable String id,
            @RequestBody @Valid UpdateTaskStatusRequestDTO request
    ) {
       var task =  updateTaskUseCase.changeStatus(ChangeTaskStatusCommand.from(id, request));
       return TaskResponseDTO.from(task);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or malformed JSON"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "409", description = "Conflict with existing academic term"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public TaskResponseDTO update(
            @PathVariable String id,
            @RequestBody @Valid UpdateTaskRequestDTO request
    ) {
        var task = updateTaskUseCase.update(UpdateTaskRequestDTO.from(id, request));
        return TaskResponseDTO.from(task);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all tasks (paged)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Page<@NotNull TaskResponseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var tasks = queryTasksUseCase.listTasksPage(TaskPageQueryCriteria.from(page, size));
        return TaskResponseDTO.from(tasks);
    }

    @GetMapping("/{id}/attempts")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all attempts for a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attempts retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<TaskAttemptResponseDTO> getAttemptsByTaskId(@PathVariable String id) {
        var attempts = taskAttemptQueryPort.findAttemptsByTaskId(id);
        return TaskAttemptResponseDTO.from(attempts);
    }
}
