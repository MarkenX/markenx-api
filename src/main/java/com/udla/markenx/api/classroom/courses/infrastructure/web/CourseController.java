package com.udla.markenx.api.classroom.courses.infrastructure.web;

import com.udla.markenx.api.classroom.courses.application.commands.ChangeCourseAcademicTermCommand;
import com.udla.markenx.api.classroom.courses.application.commands.ChangeCourseStatusCommand;
import com.udla.markenx.api.classroom.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.classroom.courses.application.commands.UpdateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.dtos.*;
import com.udla.markenx.api.classroom.courses.application.queries.GetAllCoursesPaginatedQuery;
import com.udla.markenx.api.classroom.courses.application.queries.GetCourseByIdQuery;
import com.udla.markenx.api.courses.application.commands.*;
import com.udla.markenx.api.courses.application.dtos.*;
import com.udla.markenx.api.classroom.courses.application.mappers.CourseDTOMapper;
import com.udla.markenx.api.classroom.courses.application.ports.incoming.CourseQueryUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.incoming.SaveCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.incoming.UpdateCourseUseCase;
import com.udla.markenx.api.courses.application.queries.*;
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
@RequestMapping("courses")
public class CourseController {

    private final CourseDTOMapper mapper;
    private final SaveCourseUseCase saveCourseUseCase;
    private final UpdateCourseUseCase updateCourseUseCase;
    private final CourseQueryUseCase courseQueryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully")
    })
    public CourseResponseDTO create(@RequestBody CreateCourseRequestDTO dto) {
        var command = new SaveCourseCommand(dto.name(), dto.academicTermId());
        return mapper.toDTO(saveCourseUseCase.handle(command));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a course by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No course found")
    })
    public CourseResponseDTO getById(@PathVariable String id) {
        var query = new GetCourseByIdQuery(id);
        return mapper.toDTO(updateCourseUseCase.getById(query));
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
        var command = new ChangeCourseStatusCommand(id, request.status());
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
        var command = new ChangeCourseAcademicTermCommand(id, request.academicTermId());
        return mapper.toDTO(updateCourseUseCase.changeAcademicTerm(command));
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
        var query = new GetAllCoursesPaginatedQuery(page, size);
        Page<@NotNull CourseResponseDTO> result =
                courseQueryUseCase.getAllPaginated(query).map(mapper::toDTO);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}
