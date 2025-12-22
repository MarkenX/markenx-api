package com.udla.markenx.api.academicterms.infrastructure.web;

import com.udla.markenx.api.academicterms.application.commands.ChangeAcademicTermStatusCommand;
import com.udla.markenx.api.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.academicterms.application.commands.UpdateAcademicTermCommand;
import com.udla.markenx.api.academicterms.application.dtos.UpdateAcademicTermStatusRequestDTO;
import com.udla.markenx.api.academicterms.application.dtos.CreateAcademicTermRequestDTO;
import com.udla.markenx.api.academicterms.application.dtos.UpdateAcademicTermRequestDTO;
import com.udla.markenx.api.academicterms.application.mappers.AcademicTermDTOMapper;
import com.udla.markenx.api.academicterms.application.ports.incoming.AcademicTermQueryUseCase;
import com.udla.markenx.api.academicterms.application.ports.incoming.SaveAcademicTermUseCase;
import com.udla.markenx.api.academicterms.application.ports.incoming.UpdateAcademicTermUseCase;
import com.udla.markenx.api.academicterms.application.queries.GetAcademicTermByIdQuery;
import com.udla.markenx.api.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import com.udla.markenx.api.academicterms.application.dtos.AcademicTermResponseDTO;
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

    private final AcademicTermQueryUseCase termQueryUseCase;
    private final SaveAcademicTermUseCase saveTermUseCase;
    private final UpdateAcademicTermUseCase updateTermUseCase;
    private final AcademicTermDTOMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new academic term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Academic term created successfully")
    })
    public AcademicTermResponseDTO create(@RequestBody CreateAcademicTermRequestDTO dto) {
        var command = new SaveAcademicTermCommand(dto.startDate(), dto.endDate(), dto.year(), false);
        return mapper.toDTO(saveTermUseCase.handle(command));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get an academic term by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No academic term found")
    })
    public AcademicTermResponseDTO getById(@PathVariable String id) {
        var query = new GetAcademicTermByIdQuery(id);
        return mapper.toDTO(updateTermUseCase.getById(query));
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change academic term status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term disabled successfully"),
            @ApiResponse(responseCode = "404", description = "No academic term found")
    })
    public AcademicTermResponseDTO changeStatus(
            @PathVariable String id,
            @RequestBody UpdateAcademicTermStatusRequestDTO request
    ) {
        var command = new ChangeAcademicTermStatusCommand(id, request.status());
        return mapper.toDTO(updateTermUseCase.changeStatus(command));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update academic term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term updated successfully"),
            @ApiResponse(responseCode = "404", description = "No academic term found")
    })
    public AcademicTermResponseDTO update(
            @PathVariable String id,
            @RequestBody UpdateAcademicTermRequestDTO request
    ) {
        var command = new UpdateAcademicTermCommand(id, request.startDate(), request.endDate(), request.year());
        return mapper.toDTO(updateTermUseCase.update(command));
    }

    @GetMapping
    @Operation(summary = "Get all academic terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic terms retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No academic terms found")
    })
    public ResponseEntity<@NotNull Page<@NotNull AcademicTermResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var query = new GetAllAcademicTermsPaginatedQuery(page, size);
        Page<@NotNull AcademicTermResponseDTO> result =
                termQueryUseCase.getAllPaginated(query).map(mapper::toDTO);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}
