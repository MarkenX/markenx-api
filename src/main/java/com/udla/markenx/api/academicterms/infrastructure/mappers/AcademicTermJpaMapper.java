package com.udla.markenx.api.academicterms.infrastructure.mappers;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.DateInterval;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import com.udla.markenx.api.academicterms.infrastructure.persistance.jpa.AcademicTermJpaEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class AcademicTermJpaMapper {

    public AcademicTerm toDomain(@NotNull AcademicTermJpaEntity e) {
        // Recreate domain aggregate using the existing factory/reconstitute pattern
        DateInterval interval = new DateInterval(e.getStartDate(), e.getEndDate());
        // use reconstituteFrom if you want to override status
        return AcademicTerm.reconstituteFrom(
                e.getId(), // String id
                interval,
                e.getYear(),
                e.getSequence(),
                AcademicTermStatus.valueOf(e.getStatus())
        );
    }

    public AcademicTermJpaEntity toEntity(@NotNull AcademicTerm domain) {
        return new AcademicTermJpaEntity(
                domain.getId().toString(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.getYear(),
                domain.getSequence(),
                domain.getStatus().name()
        );
    }
}
