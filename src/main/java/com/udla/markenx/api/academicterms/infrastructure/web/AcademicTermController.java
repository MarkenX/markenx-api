package com.udla.markenx.api.academicterms.infrastructure.web;

import com.udla.markenx.api.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.academicterms.application.dtos.RequestCreateAcademicTermDTO;
import com.udla.markenx.api.academicterms.application.mappers.AcademicTermDTOMapper;
import com.udla.markenx.api.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import com.udla.markenx.api.academicterms.application.services.AcademicTermQueryService;
import com.udla.markenx.api.academicterms.application.dtos.AcademicTermDTO;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/academic-terms")
public class AcademicTermController {

    private final AcademicTermQueryService service;
    private final AcademicTermDTOMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicTermDTO create(@RequestBody RequestCreateAcademicTermDTO dto) {
        var command = new SaveAcademicTermCommand(dto.startDate(), dto.endDate(), dto.year(), false);
        return mapper.toDTO(service.save(command));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<@NotNull AcademicTermDTO> readAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var query = new GetAllAcademicTermsPaginatedQuery(page, size);
        return service.getAllPaginated(query).map(mapper::toDTO);
    }
}
