package com.udla.markenx.api.courses.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import lombok.Getter;

@Getter
public class Course extends Entity {

    private final CourseId id;

    public Course(CourseId id) {
        this.id = id;
    }

    public Course(String id) {
        this.id = new CourseId(id);
    }
}
