package com.udla.markenx.api.classroom.students.domain.models.aggregates;

import com.udla.markenx.api.classroom.students.domain.exceptions.*;
import com.udla.markenx.api.classroom.students.domain.models.valueobjects.StudentStatus;
import com.udla.markenx.api.shared.domain.exceptions.EntityAlreadyDisabledException;
import com.udla.markenx.api.shared.domain.exceptions.EntityAlreadyEnabledException;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Student Aggregate")
class StudentTest {

    private static final String VALID_FIRST_NAME = "Juan";
    private static final String VALID_LAST_NAME = "Pérez";
    private static final String VALID_COURSE_ID = "course-123";

    @Nested
    @DisplayName("Factory method create")
    class FactoryMethodCreate {

        @Test
        @DisplayName("givenValidData_whenCreatingStudent_thenSucceeds")
        void givenValidData_whenCreatingStudent_thenSucceeds() {
            // When
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);

            // Then
            assertThat(student.getId()).isNotNull();
            assertThat(student.getFirstName()).isEqualTo(VALID_FIRST_NAME);
            assertThat(student.getLastName()).isEqualTo(VALID_LAST_NAME);
            assertThat(student.getCourseId()).isEqualTo(VALID_COURSE_ID);
            assertThat(student.getUserId()).isNull();
            assertThat(student.getStatusCode()).isEqualTo("PENDING_IDENTITY");
            assertThat(student.isActive()).isTrue();
        }

        @Test
        @DisplayName("givenNullCourseId_whenCreatingStudent_thenThrowsInvalidCourseIdException")
        void givenNullCourseId_whenCreatingStudent_thenThrowsInvalidCourseIdException() {
            // When & Then
            assertThatThrownBy(() -> Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, null))
                    .isInstanceOf(InvalidCourseIdException.class);
        }

        @Test
        @DisplayName("givenBlankCourseId_whenCreatingStudent_thenThrowsInvalidCourseIdException")
        void givenBlankCourseId_whenCreatingStudent_thenThrowsInvalidCourseIdException() {
            // When & Then
            assertThatThrownBy(() -> Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, "   "))
                    .isInstanceOf(InvalidCourseIdException.class);
        }

        @Test
        @DisplayName("givenNullFirstName_whenCreatingStudent_thenThrowsPersonNameCannotBeEmptyException")
        void givenNullFirstName_whenCreatingStudent_thenThrowsPersonNameCannotBeEmptyException() {
            // When & Then
            assertThatThrownBy(() -> Student.create(null, VALID_LAST_NAME, VALID_COURSE_ID))
                    .isInstanceOf(PersonNameCannotBeEmptyException.class);
        }

        @Test
        @DisplayName("givenNullLastName_whenCreatingStudent_thenThrowsPersonNameCannotBeEmptyException")
        void givenNullLastName_whenCreatingStudent_thenThrowsPersonNameCannotBeEmptyException() {
            // When & Then
            assertThatThrownBy(() -> Student.create(VALID_FIRST_NAME, null, VALID_COURSE_ID))
                    .isInstanceOf(PersonNameCannotBeEmptyException.class);
        }
    }

    @Nested
    @DisplayName("Status transitions")
    class StatusTransitions {

        @Test
        @DisplayName("givenPendingIdentityStudent_whenMarkingIdentityCreated_thenTransitionsToActive")
        void givenPendingIdentityStudent_whenMarkingIdentityCreated_thenTransitionsToActive() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);

            // When
            student.markIdentityCreated();

            // Then
            assertThat(student.getStatusCode()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("givenPendingIdentityStudent_whenMarkingIdentityCreationFailed_thenTransitionsToFailed")
        void givenPendingIdentityStudent_whenMarkingIdentityCreationFailed_thenTransitionsToFailed() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);

            // When
            student.markIdentityCreationFailed();

            // Then
            assertThat(student.getStatusCode()).isEqualTo("IDENTITY_CREATION_FAILED");
        }

        @Test
        @DisplayName("givenActiveStudent_whenMarkingIdentityCreated_thenThrowsInvalidStudentStatusTransitionException")
        void givenActiveStudent_whenMarkingIdentityCreated_thenThrowsInvalidStudentStatusTransitionException() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);
            student.markIdentityCreated();

            // When & Then
            assertThatThrownBy(student::markIdentityCreated)
                    .isInstanceOf(InvalidStudentStatusTransitionException.class);
        }

        @Test
        @DisplayName("givenActiveStudent_whenMarkingIdentityCreationFailed_thenThrowsInvalidStudentStatusTransitionException")
        void givenActiveStudent_whenMarkingIdentityCreationFailed_thenThrowsInvalidStudentStatusTransitionException() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);
            student.markIdentityCreated();

            // When & Then
            assertThatThrownBy(student::markIdentityCreationFailed)
                    .isInstanceOf(InvalidStudentStatusTransitionException.class);
        }
    }

    @Nested
    @DisplayName("Update operations")
    class UpdateOperations {

        @Test
        @DisplayName("givenStudent_whenUpdatingName_thenNameIsUpdated")
        void givenStudent_whenUpdatingName_thenNameIsUpdated() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);

            // When
            student.update("María", "González");

            // Then
            assertThat(student.getFirstName()).isEqualTo("María");
            assertThat(student.getLastName()).isEqualTo("González");
        }

        @Test
        @DisplayName("givenStudent_whenChangingCourse_thenCourseIsChanged")
        void givenStudent_whenChangingCourse_thenCourseIsChanged() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);
            String newCourseId = "course-456";

            // When
            student.changeCourse(newCourseId);

            // Then
            assertThat(student.getCourseId()).isEqualTo(newCourseId);
        }

        @Test
        @DisplayName("givenStudent_whenChangingToNullCourse_thenThrowsInvalidCourseIdException")
        void givenStudent_whenChangingToNullCourse_thenThrowsInvalidCourseIdException() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);

            // When & Then
            assertThatThrownBy(() -> student.changeCourse(null))
                    .isInstanceOf(InvalidCourseIdException.class);
        }
    }

    @Nested
    @DisplayName("User assignment")
    class UserAssignment {

        @Test
        @DisplayName("givenStudent_whenAssigningUser_thenUserIdIsSet")
        void givenStudent_whenAssigningUser_thenUserIdIsSet() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);
            String userId = "user-789";

            // When
            student.assignUser(userId);

            // Then
            assertThat(student.getUserId()).isEqualTo(userId);
        }

        @Test
        @DisplayName("givenStudent_whenAssigningNullUser_thenThrowsInvalidUserIdException")
        void givenStudent_whenAssigningNullUser_thenThrowsInvalidUserIdException() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);

            // When & Then
            assertThatThrownBy(() -> student.assignUser(null))
                    .isInstanceOf(InvalidUserIdException.class);
        }

        @Test
        @DisplayName("givenStudent_whenAssigningBlankUser_thenThrowsInvalidUserIdException")
        void givenStudent_whenAssigningBlankUser_thenThrowsInvalidUserIdException() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);

            // When & Then
            assertThatThrownBy(() -> student.assignUser("   "))
                    .isInstanceOf(InvalidUserIdException.class);
        }
    }

    @Nested
    @DisplayName("Lifecycle operations")
    class LifecycleOperations {

        @Test
        @DisplayName("givenActiveStudent_whenDisabling_thenStudentIsDisabled")
        void givenActiveStudent_whenDisabling_thenStudentIsDisabled() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);

            // When
            student.disable();

            // Then
            assertThat(student.isActive()).isFalse();
        }

        @Test
        @DisplayName("givenDisabledStudent_whenDisabling_thenThrowsEntityAlreadyDisabledException")
        void givenDisabledStudent_whenDisabling_thenThrowsEntityAlreadyDisabledException() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);
            student.disable();

            // When & Then
            assertThatThrownBy(student::disable)
                    .isInstanceOf(EntityAlreadyDisabledException.class);
        }

        @Test
        @DisplayName("givenDisabledStudent_whenEnabling_thenStudentIsEnabled")
        void givenDisabledStudent_whenEnabling_thenStudentIsEnabled() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);
            student.disable();

            // When
            student.enable();

            // Then
            assertThat(student.isActive()).isTrue();
        }

        @Test
        @DisplayName("givenActiveStudent_whenEnabling_thenThrowsEntityAlreadyEnabledException")
        void givenActiveStudent_whenEnabling_thenThrowsEntityAlreadyEnabledException() {
            // Given
            Student student = Student.create(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_COURSE_ID);

            // When & Then
            assertThatThrownBy(student::enable)
                    .isInstanceOf(EntityAlreadyEnabledException.class);
        }
    }

    @Nested
    @DisplayName("Reconstruction from persistence")
    class ReconstructionFromPersistence {

        @Test
        @DisplayName("givenPersistedData_whenReconstructingStudent_thenAllFieldsAreSet")
        void givenPersistedData_whenReconstructingStudent_thenAllFieldsAreSet() {
            // Given
            String id = "student-id-123";
            LifecycleStatus lifecycleStatus = LifecycleStatus.ACTIVE;
            long code = 1001L;
            String firstName = "Carlos";
            String lastName = "Rodríguez";
            String courseId = "course-abc";
            String userId = "user-xyz";
            StudentStatus status = StudentStatus.ACTIVE;

            // When
            Student student = new Student(id, lifecycleStatus, code, firstName, lastName, courseId, userId, status);

            // Then
            assertThat(student.getId()).isEqualTo(id);
            assertThat(student.getFirstName()).isEqualTo(firstName);
            assertThat(student.getLastName()).isEqualTo(lastName);
            assertThat(student.getCourseId()).isEqualTo(courseId);
            assertThat(student.getUserId()).isEqualTo(userId);
            assertThat(student.getStatusCode()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("givenInvalidCode_whenReconstructingStudent_thenThrowsInvalidStudentCodeException")
        void givenInvalidCode_whenReconstructingStudent_thenThrowsInvalidStudentCodeException() {
            // When & Then
            assertThatThrownBy(() -> new Student(
                    "id", LifecycleStatus.ACTIVE, 0L, "First", "Last",
                    "course", "user", StudentStatus.ACTIVE
            )).isInstanceOf(InvalidStudentCodeException.class);
        }
    }

    @Nested
    @DisplayName("Full name")
    class FullName {

        @Test
        @DisplayName("givenStudent_whenGettingFullName_thenReturnsFormattedFullName")
        void givenStudent_whenGettingFullName_thenReturnsFormattedFullName() {
            // Given
            Student student = Student.create("Juan", "Pérez", VALID_COURSE_ID);

            // When
            String fullName = student.getFullName();

            // Then
            assertThat(fullName).isEqualTo("Juan Pérez");
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringBehavior {

        @Test
        @DisplayName("givenStudent_whenCallingToString_thenReturnsFormattedCode")
        void givenStudent_whenCallingToString_thenReturnsFormattedCode() {
            // Given
            Student student = new Student(
                    "id", LifecycleStatus.ACTIVE, 42L, "First", "Last",
                    "course", "user", StudentStatus.ACTIVE
            );

            // When
            String result = student.toString();

            // Then
            assertThat(result).isEqualTo("STD-0042");
        }
    }
}
