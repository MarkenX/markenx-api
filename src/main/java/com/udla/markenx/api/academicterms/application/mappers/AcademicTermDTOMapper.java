package com.udla.markenx.api.academicterms.application.mappers;

import com.udla.markenx.api.academicterms.application.dtos.ResponseAcademicTermDTO;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import org.springframework.stereotype.Component;

@Component
public class AcademicTermDTOMapper {

    public ResponseAcademicTermDTO toDTO(AcademicTerm domain) {
        return new ResponseAcademicTermDTO(
                domain.getId().toString(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.getStatus().name(),
                domain.toString()
        );
    }
}
