package com.udla.markenx.api.courses.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import lombok.Getter;

@Getter
public class Course extends Entity {

    private final CourseId id;
    private final String name;
    private final long code;

    public Course(CourseId id, String name, long code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Course(String id, String name, long code) {
        this.id = new CourseId(id);
        this.name = name;
        this.code = code;
    }
}
