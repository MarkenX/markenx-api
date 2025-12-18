package com.udla.markenx.api.courses.domain.models.aggregates;

import com.udla.markenx.api.courses.domain.exceptions.InvalidAcademicTermIdException;
import com.udla.markenx.api.courses.domain.exceptions.InvalidCourseCodeException;
import com.udla.markenx.api.courses.domain.exceptions.InvalidCourseNameException;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

@Getter
public class Course extends Entity {

    private final CourseId id;
    private final String name;
    private final long code;

    private final String academicTermId;

    private Course(CourseId id, String name, long code, String academicTermId) {
        this.id = id;
        this.name = validateName(name);
        this.code = validateCode(code);
        this.academicTermId = validateAcademicTermId(academicTermId);
    }

    private Course(String id, String name, long code, String academicTermId) {
        this.id = new CourseId(id);
        this.name = validateName(name);
        this.code = validateCode(code);
        this.academicTermId = validateAcademicTermId(academicTermId);
    }

    // region Factories

    /**
     * Creates a new instance of the {@code Course} class with a unique identifier.
     *
     * @param name the name of the course
     * @param code the numeric code of the course
     * @param academicTermId the identifier of the associated academic term
     * @return a new {@code Course} instance
     * @throws InvalidCourseNameException if the provided {@code name} is null or contains only whitespace
     * @throws InvalidCourseCodeException if the provided {@code code} is zero or negative
     * @throws InvalidAcademicTermIdException if the provided {@code academicTermId} is null or contains only whitespace
     */
    public static @NonNull Course create(String name, long code, String academicTermId) {
        var id = CourseId.generate();
        return new Course(id, name, code, academicTermId);
    }

    // endregion

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

    /**
     * Validates the given academic term identifier to ensure it is not null or blank.
     *
     * @param academicTermId the identifier of the academic term to validate
     * @return the validated academic term identifier if it passes validation
     * @throws InvalidAcademicTermIdException if the academic term identifier is null or contains only whitespace
     */
    public String validateAcademicTermId(String academicTermId) {
        if (academicTermId == null || academicTermId.isBlank()) {
            throw new InvalidAcademicTermIdException();
        }
        return academicTermId;
    }

    // endregion

    /**
     * Formats the course code as a zero-padded four-digit string.
     *
     * @return the formatted course code as a string
     */
    @Contract(pure = true)
    private @NonNull String formatCode() {
        return String.format("%04d", code);
    }

    @Override
    public String toString() {
        return String.format("%s-%s", name, formatCode());
    }
}
