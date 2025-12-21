package com.udla.markenx.api.courses.infrastructure.persistence.jooq;

import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import org.jooq.Record;


public class CourseRecordMapper {

    public Course toDomain(Record r) {
        return new Course(
          r.get("id", String.class),
          r.get("name", String.class),
          r.get("code", Long.class),
          r.get("academic_term_id", String.class)
        );
    }

}