package com.udla.markenx.api.students.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import lombok.Getter;

@Getter
public class Student extends Entity {

    private final StudentId id;

    public Student(StudentId id) {
        this.id = id;
    }
}
