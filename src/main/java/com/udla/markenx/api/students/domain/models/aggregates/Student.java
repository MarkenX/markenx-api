package com.udla.markenx.api.students.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import com.udla.markenx.api.students.domain.exceptions.InvalidCourseIdException;
import com.udla.markenx.api.students.domain.exceptions.InvalidStudentCodeException;
import com.udla.markenx.api.students.domain.models.valueobjects.Email;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Student extends Entity {

    private final StudentId id;
    private final PersonalInfo personalInfo;
    private final Long code;
    private final String courseId;

    /**
     * Constructs a new {@code Student} instance with the specified details.
     *
     * @param id the unique identifier for the student
     * @param code the student code, which must be a positive number
     * @param firstName the first name of the student
     * @param lastName the last name of the student
     * @param email the email address of the student
     * @param courseId the identifier of the course associated with the student
     * @throws InvalidStudentCodeException if the student code is zero or negative
     * @throws InvalidCourseIdException if the course identifier is null or blank
     */
    public Student(
            StudentId id,
            Long code,
            String firstName,
            String lastName,
            Email email,
            String courseId) {
        super();
        this.id = id;
        this.code = validateCode(code);
        this.personalInfo = new PersonalInfo(firstName, lastName, email);
        this.courseId = validateCourseId(courseId);
    }

    // region Getters

    public String getId() {
        return this.id.toString();
    }

    public String getCourseId() {
        return this.courseId;
    }

    public String getFullName() {
        return this.personalInfo.getFullName();
    }

    public Email getEmail() {
        return this.personalInfo.getEmail();
    }

    // endregion

    // region Validations

    /**
     * Validates the given student code to ensure it is a positive number.
     *
     * @param code the student code to validate
     * @return the validated student code if it passes validation
     * @throws InvalidStudentCodeException if the student code is zero or negative
     */
    public long validateCode(long code) {
        if (code <= 0) {
            throw new InvalidStudentCodeException();
        }
        return code;
    }


    /**
     * Validates the given course identifier to ensure it is not null or blank.
     *
     * @param courseId the course identifier to validate
     * @return the validated course identifier if it passes validation
     * @throws InvalidCourseIdException if the course identifier is null or blank
     */
    public String validateCourseId(String courseId) {
        if (courseId == null || courseId.isBlank()) {
            throw new InvalidCourseIdException();
        }
        return courseId;
    }

    // endregion

    /**
     * Formats the student code as a zero-padded four-digit string.
     *
     * @return the formatted course code as a string
     */
    @Contract(pure = true)
    private @NonNull String formatCode() {
        return String.format("%04d", code);
    }

    @Override
    public String toString() {
        return String.format("STD-%s", formatCode());
    }
}
