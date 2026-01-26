package com.udla.markenx.api.classroom.students.infrastructure.web.rest;

import com.udla.markenx.api.classroom.students.application.ports.in.commands.ChangeStudentStatusCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.commands.UpdateStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAllTasksProgressQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAttemptsQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentProfileQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentTaskProgressQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.*;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.responses.StudentAttemptResponseDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.requests.UpdateStudentRequestDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.requests.UpdateStudentStatusRequestDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.responses.StudentProfileResponseDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.responses.StudentResponseDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.responses.StudentTaskResponseDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQuery;
import com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.responses.TermResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    private final RegisterStudentUseCase registerStudentUseCase;
    private final QueryStudentsDetailUseCase queryStudentsDetailUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;
    private final QueryStudentAttemptsUseCase queryStudentAttemptsUseCase;
    private final QueryStudentsProfileUseCase queryStudentsProfileUseCase;
    private final QueryStudentTasksProgressDetailUseCase queryStudentTasksProgressDetailUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Student already exists or conflict"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public StudentResponseDTO create(@RequestBody @Valid CreateStudentRequestDTO dto) {
        var command = new RegisterStudentCommand(
                dto.firstName(), dto.lastName(), dto.courseId(), dto.email(), false);
        return StudentResponseDTO.from(registerStudentUseCase.handle(command), dto.email());
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated student profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Student not found for authenticated user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StudentProfileResponseDTO> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = extractEmail(authentication);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var query = new StudentProfileQuery(email);
        return ResponseEntity.ok(StudentProfileResponseDTO.from(
                queryStudentsProfileUseCase.getByEmail(query)
        ));
    }

    private @Nullable String extractEmail(@NonNull Authentication authentication) {
        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            return oidcUser.getEmail();
        }
        return null;
    }

    @GetMapping("/{studentId}/attempts")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all attempts for a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attempts retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<StudentAttemptResponseDTO> getAttemptsByStudentId(@PathVariable String studentId) {
        var query = new StudentAttemptsQuery(studentId);
        return StudentAttemptResponseDTO.from(queryStudentAttemptsUseCase.getAll(query));
    }

    @GetMapping("/{studentId}/tasks/{taskId}/progress")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get student's progress on a specific task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progress retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Student or task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public StudentTaskResponseDTO getTaskProgress(
            @PathVariable String studentId,
            @PathVariable String taskId
    ) {
        var query = new StudentTaskProgressQuery(studentId, taskId);
        var taskWithProgress = queryStudentTasksProgressDetailUseCase.getTaskWithProgress(query);
        return StudentTaskResponseDTO.from(taskWithProgress);
    }

    @GetMapping("/{studentId}/tasks")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all tasks for a student with their specific progress")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks with progress retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<StudentTaskResponseDTO> getAllTasksWithProgress(
            @PathVariable String studentId
    ) {
        var query = new StudentAllTasksProgressQuery(studentId);
        var tasksWithProgress = queryStudentTasksProgressDetailUseCase.getAllTasksWithProgress(query);
        return StudentTaskResponseDTO.from(tasksWithProgress);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all students (paged)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Page<@NotNull StudentResponseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var query = new StudentPageQueryCriteria(page, size);
        return StudentResponseDTO.from(queryStudentsDetailUseCase.listStudentsPage(query));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get an student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid attemptId format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public StudentResponseDTO getById(@PathVariable String id) {
        var student = queryStudentsDetailUseCase.getStudentById(id);
        return StudentResponseDTO.from(student);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or malformed JSON"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "409", description = "Conflict with existing student"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public StudentResponseDTO update(
            @PathVariable String id,
            @RequestBody @Valid UpdateStudentRequestDTO dto) {
        var command = new UpdateStudentCommand(
                id,
                dto.firstName(),
                dto.lastName()
        );
        updateStudentUseCase.update(command);
        var studentDetail = queryStudentsDetailUseCase.getStudentById(id);
        return StudentResponseDTO.from(studentDetail);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Change student status (enable/disable)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Student status change request accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or malformed JSON"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "409", description = "Invalid status change or conflict"),
            @ApiResponse(responseCode = "422", description = "Domain rule violation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public StudentResponseDTO changeStatus(
            @PathVariable String id,
            @RequestBody @Valid UpdateStudentStatusRequestDTO request
    ) {
        updateStudentUseCase.changeStatus(ChangeStudentStatusCommand.from(id, request));
        var studentDetail = queryStudentsDetailUseCase.getStudentById(id);
        return StudentResponseDTO.from(studentDetail);
    }
}
