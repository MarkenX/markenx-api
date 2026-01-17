package com.udla.markenx.api.classroom.terms.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.terms.domain.models.valueobjects.TermStatus;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jooq.Record;

import java.time.LocalDate;

public class AcademicTermRecordMapper {

    public AcademicTerm toDomain(Record r) {

        return new AcademicTerm(
                r.get("id", String.class),
                LifecycleStatus.valueOf(r.get("lifecycle_status", String.class)),
                r.get("start_date", LocalDate.class),
                r.get("end_date", LocalDate.class),
                r.get("academic_year", Integer.class),
                r.get("sequence", Integer.class),
                TermStatus.valueOf(r.get("status", String.class))
        );
    }
}