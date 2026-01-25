package com.udla.markenx.api.classroom.terms.infrastructure.web.rest;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.ChangeTermStatusCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.commands.CreateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.commands.UpdateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQuery;
import com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.requests.CreateTermRequestDTO;
import com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.requests.UpdateTermRequestDTO;
import com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.requests.UpdateTermStatusRequestDTO;
import com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.responses.CreateTermResponseDTO;
import com.udla.markenx.api.classroom.terms.infrastructure.web.dtos.responses.TermResponseDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.QueryTermsUseCase;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.CreateTermUseCase;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.UpdateTermUseCase;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermPageQueryCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("academic-terms")
public class TermController {

    private final QueryTermsUseCase queryTermsUseCase;
    private final CreateTermUseCase createTermUseCase;
    private final UpdateTermUseCase updateTermUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new academic term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Academic term created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Academic term already exists or conflict"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CreateTermResponseDTO create(@RequestBody @Valid CreateTermRequestDTO request) {
        var newTerm = createTermUseCase.handle(CreateTermCommand.from(request));
        return CreateTermResponseDTO.from(newTerm);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get an academic term by attemptId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid attemptId format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Academic term not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public TermResponseDTO getById(@PathVariable String id) {
        var term = queryTermsUseCase.getTermById(TermIdQuery.from(id));
        return TermResponseDTO.from(term);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change academic term status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or malformed JSON"),
            @ApiResponse(responseCode = "404", description = "Academic term not found"),
            @ApiResponse(responseCode = "409", description = "Invalid status change or conflict"),
            @ApiResponse(responseCode = "422", description = "Domain rule violation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public TermResponseDTO changeStatus(
            @PathVariable String id,
            @RequestBody @Valid UpdateTermStatusRequestDTO request
    ) {
        var term = updateTermUseCase.changeStatus(ChangeTermStatusCommand.from(id, request));
        return TermResponseDTO.from(term);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update academic term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic term updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or malformed JSON"),
            @ApiResponse(responseCode = "404", description = "Academic term not found"),
            @ApiResponse(responseCode = "409", description = "Conflict with existing academic term"),
            @ApiResponse(responseCode = "422", description = "Domain validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public TermResponseDTO update(
            @PathVariable String id,
            @RequestBody @Valid UpdateTermRequestDTO request
    ) {
        var term = updateTermUseCase.update(UpdateTermCommand.from(id, request));
        return TermResponseDTO.from(term);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all academic terms (paged)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Academic terms retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Page<TermResponseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var terms = queryTermsUseCase.listTermsPage(TermPageQueryCriteria.from(page, size));
        return TermResponseDTO.from(terms);
    }
}
