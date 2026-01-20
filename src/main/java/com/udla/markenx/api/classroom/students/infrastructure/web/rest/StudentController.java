package com.udla.markenx.api.classroom.students.infrastructure.web.rest;

import com.udla.markenx.api.classroom.students.application.ports.in.commands.DisableStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.commands.UpdateStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.RegisterStudentUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentsUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.UpdateStudentUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.CreateStudentRequestDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentAttemptResponseDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentCourseResponseDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentMeResponseDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentResponseDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.UpdateStudentRequestDTO;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.AttemptQueryUseCase;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.mappers.StudentResponseDTOMapper;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.mappers.StudentUserRedDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("students")
public class StudentController {

    private final StudentResponseDTOMapper responseDTOMapper;
    private final StudentUserRedDTOMapper userDTOMapper;
    private final RegisterStudentUseCase registerStudentUseCase;
    private final QueryStudentsUseCase queryStudentsUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;
    private final AttemptQueryUseCase attemptQueryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully")
    })
    public StudentResponseDTO create(@RequestBody CreateStudentRequestDTO dto) {
        var command = new RegisterStudentCommand(
                dto.firstName(), dto.lastName(), dto.courseId(), dto.email(), false);
        return responseDTOMapper.toDTO(registerStudentUseCase.handle(command), dto.email());
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated student profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Student not found for authenticated user")
    })
    public ResponseEntity<StudentMeResponseDTO> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = extractEmail(authentication);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return queryStudentsUseCase.findByEmail(email)
                .map(student -> ResponseEntity.ok(
                        new StudentMeResponseDTO(
                                student.studentId(),
                                student.email(),
                                student.fullName()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    private String extractEmail(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            return oidcUser.getEmail();
        }
        return null;
    }

    @GetMapping("/{studentId}/course")
    @Operation(summary = "Get course information for a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course information retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Student or course not found")
    })
    public ResponseEntity<StudentCourseResponseDTO> getStudentCourse(@PathVariable String studentId) {
        return queryStudentsUseCase.findCourseByStudentId(studentId)
                .map(courseInfo -> ResponseEntity.ok(
                        new StudentCourseResponseDTO(
                                courseInfo.courseId(),
                                courseInfo.courseName(),
                                courseInfo.term()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{studentId}/attempts")
    @Operation(summary = "Get all attempts for a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attempts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No attempts found for student")
    })
    public ResponseEntity<List<StudentAttemptResponseDTO>> getAttemptsByStudentId(@PathVariable String studentId) {
        var attempts = attemptQueryUseCase.getByStudentId(studentId);
        if (attempts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attempts.stream()
                .map(attempt -> new StudentAttemptResponseDTO(
                        attempt.attemptId(),
                        attempt.taskId(),
                        attempt.evaluatedAt(),
                        attempt.evaluatedAt(),
                        attempt.finalOutcome(),
                        attempt.finalOutcome(),
                        attempt.finalAcceptance()
                ))
                .toList());
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
        var query = new StudentPageQueryCriteria(page, size);
        Page<@NotNull StudentUserReadDTO> result =
                queryStudentsUseCase.getAllPaginated(query).map(userDTOMapper::toDTO);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public StudentResponseDTO update(
            @PathVariable String id,
            @RequestBody UpdateStudentRequestDTO dto
    ) {
        var command = new UpdateStudentCommand(
                id,
                dto.firstName(),
                dto.lastName(),
                dto.courseId()
        );
        var student = updateStudentUseCase.update(command);
        return responseDTOMapper.toDTO(student, null);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Disable a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Student disable request accepted"),
            @ApiResponse(responseCode = "400", description = "Student cannot be disabled"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public void disable(@PathVariable String id) {
        var command = new DisableStudentCommand(id);
        updateStudentUseCase.disable(command);
    }
}
