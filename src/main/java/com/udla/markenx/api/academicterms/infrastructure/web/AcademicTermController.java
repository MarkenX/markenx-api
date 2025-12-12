package com.udla.markenx.api.academicterms.infrastructure.web;

import com.udla.markenx.api.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import com.udla.markenx.api.academicterms.application.services.AcademicTermQueryService;
import com.udla.markenx.api.academicterms.application.dtos.AcademicTermDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/academic-terms")
public class AcademicTermController {

    private final AcademicTermQueryService service;

    public AcademicTermController(AcademicTermQueryService service) {
        this.service = service;
    }

    @GetMapping
    public Page<@NotNull AcademicTermDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var query = new GetAllAcademicTermsPaginatedQuery(page, size);
        return service.getAllPaginated(query);
    }
}
