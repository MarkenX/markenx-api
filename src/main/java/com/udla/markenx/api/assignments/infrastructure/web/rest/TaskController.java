package com.udla.markenx.api.assignments.infrastructure.web.rest;

import com.udla.markenx.api.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.assignments.application.ports.incoming.SaveTaskUseCase;
import com.udla.markenx.api.assignments.application.ports.incoming.TaskQueryUseCase;
import com.udla.markenx.api.assignments.infrastructure.web.rest.dtos.CreateTaskRequestDTO;
import com.udla.markenx.api.assignments.infrastructure.web.rest.dtos.TaskResponseDTO;
import com.udla.markenx.api.assignments.infrastructure.web.rest.mappers.TaskResponseDTOMapper;
import com.udla.markenx.api.students.application.commands.RegisterStudentCommand;
import com.udla.markenx.api.students.infrastructure.web.rest.dtos.CreateStudentRequestDTO;
import com.udla.markenx.api.students.infrastructure.web.rest.dtos.StudentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
