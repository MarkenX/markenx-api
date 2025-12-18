package com.udla.markenx.api.courses.domain.models.aggregates;

import com.udla.markenx.api.courses.domain.exceptions.InvalidCourseCodeException;
import com.udla.markenx.api.courses.domain.exceptions.InvalidCourseNameException;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import lombok.Getter;

@Getter
public class Course extends Entity {

    private final CourseId id;
    private final String name;
    private final long code;

    private final String academicTermId;

    public Course(CourseId id, String name, long code, String academicTermId) {
        this.id = id;
        this.name = validateName(name);
        this.code = validateCode(code);
        this.academicTermId = academicTermId;
    }

    public Course(String id, String name, long code, String academicTermId) {
        this.id = new CourseId(id);
        this.name = validateName(name);
        this.code = validateCode(code);
        this.academicTermId = academicTermId;
    }

    // region Validations

    /**
     * Validates the given course name to ensure it is not null or blank.
     *
     * @param name the name of the course to validate
     * @return the validated course name if it passes validation
     * @throws InvalidCourseNameException if the course name is null or contains only whitespace
     */
    public String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidCourseNameException();
        }
        return name;
    }

    /**
     * Validates the given course code to ensure it is a positive number.
     *
     * @param code the course code to validate
     * @return the validated course code if it passes validation
     * @throws InvalidCourseCodeException if the course code is zero or negative
     */
    public long validateCode(long code) {
        if (code <= 0) {
            throw new InvalidCourseCodeException();
        }
        return code;
    }

    // endregion
}
