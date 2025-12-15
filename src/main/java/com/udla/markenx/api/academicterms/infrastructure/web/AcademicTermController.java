package com.udla.markenx.api.academicterms.infrastructure.web;

import com.udla.markenx.api.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.academicterms.application.dtos.RequestCreateAcademicTermDTO;
import com.udla.markenx.api.academicterms.application.mappers.AcademicTermDTOMapper;
import com.udla.markenx.api.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import com.udla.markenx.api.academicterms.application.services.AcademicTermQueryService;
import com.udla.markenx.api.academicterms.application.dtos.ResponseAcademicTermDTO;
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
@RequestMapping("academic-terms")
public class AcademicTermController {

    private final AcademicTermQueryService service;
    private final AcademicTermDTOMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new academic term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Academic term created successfully")
    })
    public ResponseAcademicTermDTO create(@RequestBody RequestCreateAcademicTermDTO dto) {
        var command = new SaveAcademicTermCommand(dto.startDate(), dto.endDate(), dto.year(), false);
        return mapper.toDTO(service.save(command));
    }

    @GetMapping
    @Operation(summary = "Get all academic terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic terms retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No academic terms found")
    })
    public ResponseEntity<@NotNull Page<@NotNull ResponseAcademicTermDTO>> readAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var query = new GetAllAcademicTermsPaginatedQuery(page, size);
        Page<@NotNull ResponseAcademicTermDTO> result =
                service.getAllPaginated(query).map(mapper::toDTO);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }
}
