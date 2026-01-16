package com.udla.markenx.api.classroom.academicterms.infrastructure.web.mappers;

import com.udla.markenx.api.classroom.academicterms.infrastructure.web.dtos.AcademicTermResponseDTO;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;
import org.springframework.stereotype.Component;

@Component
public class AcademicTermDTOMapper {

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
