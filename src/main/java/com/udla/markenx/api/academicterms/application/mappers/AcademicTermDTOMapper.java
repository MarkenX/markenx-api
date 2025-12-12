package com.udla.markenx.api.academicterms.application.mappers;

import com.udla.markenx.api.academicterms.application.dtos.AcademicTermDTO;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import org.springframework.stereotype.Component;

@Component
public class AcademicTermDTOMapper {

    public AcademicTermDTO toDTO(AcademicTerm domain) {
        return new AcademicTermDTO(
                domain.getId().toString(),
                domain.getYear(),
                domain.getSequence(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.isActive() ? "ACTIVE" : (domain.isUpcoming() ? "UPCOMING" : "ENDED")
        );
    }
}
