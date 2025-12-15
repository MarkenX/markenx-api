package com.udla.markenx.api.academicterms.infrastructure.mappers;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.DateInterval;
import com.udla.markenx.api.academicterms.infrastructure.persistance.jpa.AcademicTermJpaEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class AcademicTermJpaMapper {

    public AcademicTerm toDomain(@NotNull AcademicTermJpaEntity e) {
        DateInterval interval = new DateInterval(e.getStartDate(), e.getEndDate());
        return new AcademicTerm(
                e.getId(),
                e.getLifecycleStatus(),
                interval,
                e.getYear(),
                e.getSequence(),
                e.getStatus()
        );
    }

    public AcademicTermJpaEntity toEntity(@NotNull AcademicTerm domain) {
        return new AcademicTermJpaEntity(
                domain.getId().toString(),
                domain.getLifecycleStatus(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.getYear(),
                domain.getSequence(),
                domain.getStatus()
        );
    }
}
