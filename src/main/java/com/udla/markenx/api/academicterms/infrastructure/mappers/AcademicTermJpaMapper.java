package com.udla.markenx.api.academicterms.infrastructure.mappers;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTermId;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.DateInterval;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import com.udla.markenx.api.academicterms.infrastructure.persistance.jpa.AcademicTermJpaEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class AcademicTermJpaMapper {

    public AcademicTerm toDomain(@NotNull AcademicTermJpaEntity e, int sequence) {
        DateInterval interval = new DateInterval(e.getStartDate(), e.getEndDate());
        return new AcademicTerm(
                e.getId(),
                interval,
                e.getYear(),
                sequence,
                AcademicTermStatus.valueOf(e.getStatus())
        );
    }

    public AcademicTermJpaEntity toEntity(@NotNull AcademicTerm domain) {
        return new AcademicTermJpaEntity(
                domain.getId().toString(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.getYear(),
                domain.getStatus().name()
        );
    }
}
