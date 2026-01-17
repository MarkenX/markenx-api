package com.udla.markenx.api.classroom.academicterms.infrastructure.web.rest;

import com.udla.markenx.api.classroom.academicterms.application.ports.in.commands.ChangeTermStatusCommand;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.commands.CreateTermCommand;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.commands.UpdateTermCommand;
import com.udla.markenx.api.classroom.academicterms.infrastructure.web.dtos.*;
import com.udla.markenx.api.classroom.academicterms.infrastructure.web.mappers.TermControllerMapper;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.usecases.ListTermsUseCase;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.usecases.CreateTermUseCase;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.usecases.UpdateTermUseCase;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.queries.TermIdQueryCriteria;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.queries.TermPageQueryCriteria;
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
public class TermController {

    private final ListTermsUseCase query;
    private final CreateTermUseCase createTerm;
    private final UpdateTermUseCase updateTerm;
    private final TermControllerMapper mapper = new TermControllerMapper();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new academic term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Academic term created successfully")
    })
    public TermDetailResponseDTO create(@RequestBody CreateTermRequestDTO dto) {
        var command = new CreateTermCommand(dto.startDate(), dto.endDate(), dto.year(), false);
        return mapper.toDetailResponseDTO(createTerm.handle(command));
    }

    @GetMapping("/active")
    @Operation(summary = "obtain the current academic term")
    public TermResponseDTO getActiveTerm() {
        return mapper.toResponseDTO(query.getActiveTerm());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get an academic term by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No academic term found")
    })
    public TermDetailResponseDTO getById(@PathVariable String id) {
        var query = new TermIdQueryCriteria(id);
        return mapper.toDetailResponseDTO(updateTerm.getById(query));
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change academic term status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term disabled successfully"),
            @ApiResponse(responseCode = "404", description = "No academic term found")
    })
    public TermDetailResponseDTO changeStatus(
            @PathVariable String id,
            @RequestBody UpdateTermStatusRequestDTO request
    ) {
        var command = new ChangeTermStatusCommand(id, request.status());
        return mapper.toDetailResponseDTO(updateTerm.changeStatus(command));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update academic term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term updated successfully"),
            @ApiResponse(responseCode = "404", description = "No academic term found")
    })
    public TermDetailResponseDTO update(
            @PathVariable String id,
            @RequestBody UpdateTermRequestDTO request
    ) {
        var command = new UpdateTermCommand(id, request.startDate(), request.endDate(), request.year());
        return mapper.toDetailResponseDTO(updateTerm.update(command));
    }

    @GetMapping
    @Operation(summary = "Get all academic terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic terms retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No academic terms found")
    })
    public ResponseEntity<@NotNull Page<@NotNull TermDetailResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var criteria = new TermPageQueryCriteria(page, size);
        Page<TermDetailResponseDTO> terms = query.listTermsPage(criteria).map(mapper::toDetailResponseDTO);

        if (terms.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(terms);
    }
}
