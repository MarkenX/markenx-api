package com.udla.markenx.api.students.infrastructure.web;

import com.udla.markenx.api.students.application.commands.SaveStudentCommand;
import com.udla.markenx.api.students.application.dtos.CreateStudentRequestDTO;
import com.udla.markenx.api.students.application.dtos.StudentResponseDTO;
import com.udla.markenx.api.students.application.mappers.StudentDTOMapper;
import com.udla.markenx.api.students.application.ports.incoming.SaveStudentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("students")
public class StudentController {

    private final StudentDTOMapper mapper;
    private final SaveStudentUseCase saveTermUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully")
    })
    public StudentResponseDTO create(@RequestBody CreateStudentRequestDTO dto) {
        var command = new SaveStudentCommand(
                dto.firstName(), dto.lastName(), dto.email(), dto.courseId());
        return mapper.toDTO(saveTermUseCase.handle(command));
    }
}
