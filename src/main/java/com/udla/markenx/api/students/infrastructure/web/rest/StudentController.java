package com.udla.markenx.api.students.infrastructure.web.rest;

import com.udla.markenx.api.students.application.commands.RegisterStudentCommand;
import com.udla.markenx.api.students.infrastructure.web.rest.dtos.CreateStudentRequestDTO;
import com.udla.markenx.api.students.infrastructure.web.rest.dtos.StudentResponseDTO;
import com.udla.markenx.api.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import com.udla.markenx.api.students.infrastructure.web.rest.mappers.StudentResponseDTOMapper;
import com.udla.markenx.api.students.application.ports.incoming.RegisterStudentUseCase;
import com.udla.markenx.api.students.application.ports.incoming.StudentQueryUseCase;
import com.udla.markenx.api.students.application.queries.GetAllStudentsPaginatedQuery;
import com.udla.markenx.api.students.infrastructure.web.rest.mappers.StudentUserRedDTOMapper;
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
@RequestMapping("students")
public class StudentController {

    private final StudentResponseDTOMapper responseDTOMapper;
    private final StudentUserRedDTOMapper userDTOMapper;
    private final RegisterStudentUseCase registerStudentUseCase;
    private final StudentQueryUseCase studentQueryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully")
    })
    public StudentResponseDTO create(@RequestBody CreateStudentRequestDTO dto) {
        var command = new RegisterStudentCommand(
                dto.firstName(), dto.lastName(), dto.courseId(), dto.email());
        return responseDTOMapper.toDTO(registerStudentUseCase.handle(command), dto.email());
    }

    @GetMapping
    @Operation(summary = "Get all students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No students found")
    })
    public ResponseEntity<@NotNull Page<@NotNull StudentUserReadDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var query = new GetAllStudentsPaginatedQuery(page, size);
        Page<@NotNull StudentUserReadDTO> result =
                studentQueryUseCase.getAllPaginated(query).map(userDTOMapper::toDTO);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}
