package com.udla.markenx.api.classroom.academicterms.infrastructure.web.mappers;

import com.udla.markenx.api.classroom.academicterms.application.ports.in.dtos.TermPortDTO;
import org.springframework.stereotype.Component;

@Component
public class TermControllerMapper {

    public AcademicTermResponseDTO toDTO(AcademicTerm domain) {
        return new AcademicTermResponseDTO(
                domain.getId().toString(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.getStatus().name(),
                domain.toString()
        );
    }
}
