package com.udla.markenx.api.students.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import lombok.Getter;

@Getter
public class Student extends Entity {

    private final StudentId id;
    private PersonalInfo personalInfo;
    private Long code;
    private final String courseId;

    public Student(StudentId id, String courseId) {
        this.id = id;
        this.courseId = courseId;
    }
}
