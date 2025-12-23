package com.udla.markenx.api.students.infrastructure.web;

import com.udla.markenx.api.students.application.commands.SaveStudentCommand;
import com.udla.markenx.api.students.application.dtos.CreateStudentRequestDTO;
import com.udla.markenx.api.students.application.dtos.StudentResponseDTO;
import com.udla.markenx.api.students.application.mappers.StudentDTOMapper;
import com.udla.markenx.api.students.application.ports.incoming.SaveStudentUseCase;
import com.udla.markenx.api.students.application.ports.incoming.StudentQueryUseCase;
import com.udla.markenx.api.students.application.queries.GetAllStudentsPaginatedQuery;
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

    private final StudentDTOMapper mapper;
    private final SaveStudentUseCase saveTermUseCase;
    private final StudentQueryUseCase studentQueryUseCase;

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

    @GetMapping
    @Operation(summary = "Get all students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No students found")
    })
    public ResponseEntity<@NotNull Page<@NotNull StudentResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var query = new GetAllStudentsPaginatedQuery(page, size);
        Page<@NotNull StudentResponseDTO> result =
                studentQueryUseCase.getAllPaginated(query).map(mapper::toDTO);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}
