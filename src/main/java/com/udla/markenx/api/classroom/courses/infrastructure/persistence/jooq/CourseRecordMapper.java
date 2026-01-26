package com.udla.markenx.api.classroom.courses.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jooq.Record;


public class CourseRecordMapper {

    public Course toDomain(Record r) {
        return new Course(
          r.get("id", String.class),
          LifecycleStatus.valueOf(r.get("lifecycle_status", String.class)),
          r.get("name", String.class),
          r.get("code", Long.class),
          r.get("academic_term_id", String.class)
        );
    }

}