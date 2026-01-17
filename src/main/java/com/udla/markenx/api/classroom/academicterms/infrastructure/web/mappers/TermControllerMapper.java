package com.udla.markenx.api.classroom.academicterms.infrastructure.web.mappers;

import com.udla.markenx.api.classroom.academicterms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.academicterms.infrastructure.web.dtos.TermDetailResponseDTO;
import com.udla.markenx.api.classroom.academicterms.infrastructure.web.dtos.TermResponseDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TermControllerMapper {

    public TermResponseDTO toResponseDTO(@NonNull TermPortDTO query) {
        return new TermResponseDTO(query.id(), query.toString());
    }

    public TermDetailResponseDTO toDetailResponseDTO(@NonNull TermPortDTO query) {
        return new TermDetailResponseDTO(
                query.id(),
                query.startDate(),
                query.endDate(),
                query.status(),
                query.toString()
        );
    }
}
