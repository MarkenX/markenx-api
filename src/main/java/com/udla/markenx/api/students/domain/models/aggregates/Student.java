package com.udla.markenx.api.students.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import com.udla.markenx.api.students.domain.exceptions.InvalidCourseIdException;
import com.udla.markenx.api.students.domain.exceptions.InvalidStudentCodeException;
import com.udla.markenx.api.students.domain.exceptions.InvalidUserIdException;
import com.udla.markenx.api.students.domain.models.valueobjects.StudentStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Student extends Entity {

    private final StudentId id;
    private final PersonalInfo personalInfo;
    private final String courseId;
    private final String userId;

    private long code;
    private StudentStatus status;

    // region Constructors

    /**
     * Constructs a new {@code Student} instance with the specified details.
     *
     * @param id the unique identifier for the student
     * @param firstName the first name of the student
     * @param lastName the last name of the student
     * @param courseId the identifier of the course associated with the student
     * @throws InvalidStudentCodeException if the student code is zero or negative
     * @throws InvalidCourseIdException if the course identifier is null or blank
     */
    private Student(
            StudentId id,
            String firstName,
            String lastName,
            String courseId) {
        super();
        this.id = id;
        this.personalInfo = new PersonalInfo(firstName, lastName);
        this.courseId = validateCourseId(courseId);
        this.userId = null;
        this.status = StudentStatus.PENDING_IDENTITY;
    }

    /**
     * Constructs a new {@code Student} instance with the specified details.
     *
     * @param id the unique identifier for the student
     * @param lifecycleStatus the lifecycle status of the student, either ACTIVE or DISABLED
     * @param code the unique numeric code for the student; must be greater than zero
     * @param firstName the first name of the student
     * @param lastName the last name of the student
     * @param courseId the identifier of the course associated with the student
     * @param userId the unique identifier for the user associated with the student
     * @throws InvalidStudentCodeException if the student code is zero or negative
     * @throws InvalidCourseIdException if the course identifier is null or blank
     * @throws InvalidUserIdException if the user identifier is null or blank
     */
    public Student(
            String id,
            LifecycleStatus lifecycleStatus,
            long code,
            String firstName,
            String lastName,
            String courseId,
            String userId) {
        super(lifecycleStatus);
        this.id = new StudentId(id);
        this.code = validateCode(code);
        this.personalInfo = new PersonalInfo(firstName, lastName);
        this.courseId = validateCourseId(courseId);
        this.userId = userId;
    }

    // endregion

    // region Factories

    public static @NonNull Student create(String firstName, String lastName, String courseId) {
        var id = StudentId.generate();
        return new Student(id, firstName, lastName, courseId);
    }

    // endregion

    /**
     * Updates the student's personal information, including their name and email address.
     *
     * @param firstName the new first name of the student
     * @param lastName the new last name of the student
     */
    public void update(String firstName, String lastName) {
        this.personalInfo.updateName(firstName, lastName);
    }

    // region Getters

    public String getId() {
        return this.id.toString();
    }

    public String getCourseId() {
        return this.courseId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return this.personalInfo.getFirstName();
    }

    public String getLastName() {
        return this.personalInfo.getLastName();
    }

    public String getFullName() {
        return this.personalInfo.getFullName();
    }

    public String getStatusCode() {
        return this.status.name();
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

    /**
     * Validates the given user identifier to ensure it is not null or blank.
     *
     * @param userId the user identifier to validate
     * @return the validated user identifier if it passes validation
     * @throws InvalidUserIdException if the user identifier is null or blank
     */
    public String validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidUserIdException();
        }
        return userId;
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
