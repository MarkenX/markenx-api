package com.udla.markenx.api.courses.infrastructure.web;

import com.udla.markenx.api.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.courses.application.dtos.CourseResponseDTO;
import com.udla.markenx.api.courses.application.dtos.CreateCourseRequestDTO;
import com.udla.markenx.api.courses.application.mappers.CourseDTOMapper;
import com.udla.markenx.api.courses.application.ports.incoming.SaveCourseUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("academic-terms")
public class CourseController {

    private final CourseDTOMapper mapper;
    private final SaveCourseUseCase saveCourseUseCase;

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
}
