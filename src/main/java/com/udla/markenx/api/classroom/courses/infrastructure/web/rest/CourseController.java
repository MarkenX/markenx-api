package com.udla.markenx.api.classroom.courses.infrastructure.web.rest;

import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskCourseIdQueryCriteria;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.QueryTasksUseCase;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.TaskResponseDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.mappers.TaskDTOMapper;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeTermCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeStatusCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.CreateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.UpdateCourseCommand;
import com.udla.markenx.api.classroom.courses.infrastructure.web.mappers.CourseDTOMapper;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.CreateCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.UpdateCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CoursePageQueryCriteria;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQuery;
import com.udla.markenx.api.classroom.courses.infrastructure.web.dtos.*;
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
@RequestMapping("courses")
public class CourseController {

    private final CourseDTOMapper mapper;
    private final TaskDTOMapper taskMapper;
    private final CreateCourseUseCase createCourseUseCase;
    private final UpdateCourseUseCase updateCourseUseCase;
    private final QueryCourseUseCase queryCourseUseCase;
    private final QueryCourseUseCase courseQueryUseCase;
    private final QueryTasksUseCase queryTasksUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully")
    })
    public CourseResponseDTO create(@RequestBody CreateCourseRequestDTO dto) {
        var command = new CreateCourseCommand(dto.name(), dto.academicTermId(), false);
        return mapper.toDTO(createCourseUseCase.handle(command));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a course by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No course found")
    })
    public CourseResponseDTO getById(@PathVariable String id) {
        var query = new CourseIdQuery(id);
        return mapper.toDTO(queryCourseUseCase.getCourseById(query));
    }

    @GetMapping("/{courseId}/tasks")
    @Operation(summary = "Get all tasks for a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No tasks found for course")
    })
    public ResponseEntity<List<TaskResponseDTO>> getTasksByCourseId(@PathVariable String courseId) {
        var criteria = new TaskCourseIdQueryCriteria(courseId);
        var tasks = queryTasksUseCase.listTasksByCourseId(criteria);
        if (tasks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tasks.stream().map(taskMapper::toResponseDTO).toList());
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change course status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course disabled successfully"),
            @ApiResponse(responseCode = "404", description = "No course found")
    })
    public CourseResponseDTO changeStatus(
            @PathVariable String id,
            @RequestBody UpdateCourseStatusRequestDTO request
    ) {
        var command = new ChangeStatusCommand(id, request.status());
        return mapper.toDTO(updateCourseUseCase.changeStatus(command));
    }

    @PutMapping("/{id}/change-academic-term")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change course related academic term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term changed successfully"),
            @ApiResponse(responseCode = "404", description = "No course found")
    })
    public CourseResponseDTO changeAcademicTerm(
            @PathVariable String id,
            @RequestBody UpdateCourseAcademicTermRequestDTO request
    ) {
        var command = new ChangeTermCommand(id, request.academicTermId());
        return mapper.toDTO(updateCourseUseCase.changeTerm(command));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "404", description = "No course found")
    })
    public CourseResponseDTO update(
            @PathVariable String id,
            @RequestBody UpdateCourseRequestDTO request
    ) {
        var command = new UpdateCourseCommand(id, request.name());
        return mapper.toDTO(updateCourseUseCase.update(command));
    }

    @GetMapping
    @Operation(summary = "Get all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No courses found")
    })
    public ResponseEntity<@NotNull Page<@NotNull CourseResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var query = new CoursePageQueryCriteria(page, size);
        Page<@NotNull CourseResponseDTO> result =
                courseQueryUseCase.listCoursesPage(query).map(mapper::toDTO);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}
