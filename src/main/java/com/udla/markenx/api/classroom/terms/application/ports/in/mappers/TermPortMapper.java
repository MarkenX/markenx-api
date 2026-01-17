package com.udla.markenx.api.classroom.terms.application.ports.in.mappers;

import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;
import org.jspecify.annotations.NonNull;

public class TermPortMapper {

    public TermPortDTO toDTO(@NonNull AcademicTerm domain) {
        return new TermPortDTO(
                domain.getId().toString(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.getStatus().name(),
                domain.toString()
        );
    }

}
