package com.udla.markenx.api.classroom.courses.infrastructure.web.rest;

import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskCourseIdQueryCriteria;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.QueryTasksUseCase;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.responses.TaskResponseDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeTermCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeStatusCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.CreateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.UpdateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.CreateCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.UpdateCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CoursePageQueryCriteria;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQuery;
import com.udla.markenx.api.classroom.courses.infrastructure.web.dtos.*;
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
@RequestMapping("courses")
public class CourseController {

    private final CreateCourseUseCase createCourseUseCase;
    private final UpdateCourseUseCase updateCourseUseCase;
    private final QueryCourseUseCase queryCourseUseCase;
    private final QueryTasksUseCase queryTasksUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Course already exists or conflict"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CourseResponseDTO create(@RequestBody @Valid CreateCourseRequestDTO dto) {
        var command = new CreateCourseCommand(dto.name(), dto.academicTermId(), false);
        return CourseResponseDTO.from(createCourseUseCase.handle(command));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a course by attemptId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid attemptId format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CourseResponseDTO getById(@PathVariable String id) {
        var query = new CourseIdQuery(id);
        return CourseResponseDTO.from(queryCourseUseCase.getCourseById(query));
    }

    @GetMapping("/{courseId}/tasks")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all tasks for a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<TaskResponseDTO> getTasksByCourseId(@PathVariable String courseId) {
        var criteria = new TaskCourseIdQueryCriteria(courseId);
        var tasks = queryTasksUseCase.listTasksByCourseId(criteria);
        return tasks.stream().map(TaskResponseDTO::from).toList();
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change course status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or malformed JSON"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Invalid status change or conflict"),
            @ApiResponse(responseCode = "422", description = "Domain rule violation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CourseResponseDTO changeStatus(
            @PathVariable String id,
            @RequestBody @Valid UpdateCourseStatusRequestDTO request
    ) {
        var command = new ChangeStatusCommand(id, request.status());
        return CourseResponseDTO.from(updateCourseUseCase.changeStatus(command));
    }

    @PutMapping("/{id}/change-academic-term")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change course related academic term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or malformed JSON"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Conflict with academic term"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CourseResponseDTO changeAcademicTerm(
            @PathVariable String id,
            @RequestBody @Valid UpdateCourseAcademicTermRequestDTO request
    ) {
        var command = new ChangeTermCommand(id, request.academicTermId());
        return CourseResponseDTO.from(updateCourseUseCase.changeTerm(command));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or malformed JSON"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Conflict with existing course"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CourseResponseDTO update(
            @PathVariable String id,
            @RequestBody @Valid UpdateCourseRequestDTO request
    ) {
        var command = new UpdateCourseCommand(id, request.name());
        return CourseResponseDTO.from(updateCourseUseCase.update(command));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all courses (paged)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Page<@NotNull CourseResponseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var query = new CoursePageQueryCriteria(page, size);
        return CourseResponseDTO.from(queryCourseUseCase.listCoursesPage(query));
    }
}
