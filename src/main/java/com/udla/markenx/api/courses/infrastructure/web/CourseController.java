package com.udla.markenx.api.courses.infrastructure.web;

import com.udla.markenx.api.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.courses.application.dtos.CourseResponseDTO;
import com.udla.markenx.api.courses.application.dtos.CreateCourseRequestDTO;
import com.udla.markenx.api.courses.application.mappers.CourseDTOMapper;
import com.udla.markenx.api.courses.application.ports.incoming.SaveCourseUseCase;
import com.udla.markenx.api.courses.application.ports.incoming.UpdateCourseUseCase;
import com.udla.markenx.api.courses.application.queries.GetCourseByIdQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("courses")
public class CourseController {

    private final CourseDTOMapper mapper;
    private final SaveCourseUseCase saveCourseUseCase;
    private final UpdateCourseUseCase updateCourseUseCase;

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
}
