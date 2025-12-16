package com.udla.markenx.api.academicterms.application.mappers;

import com.udla.markenx.api.academicterms.application.dtos.AcademicTermResponseDTO;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
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
