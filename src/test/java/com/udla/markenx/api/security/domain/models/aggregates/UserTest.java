package com.udla.markenx.api.security.domain.models.aggregates;

import com.udla.markenx.api.security.domain.models.valueobjects.Email;
import com.udla.markenx.api.security.domain.models.valueobjects.Role;
import com.udla.markenx.api.shared.domain.exceptions.EntityAlreadyDisabledException;
import com.udla.markenx.api.shared.domain.exceptions.EntityAlreadyEnabledException;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("User Aggregate")
class UserTest {

    private static final Set<String> ALLOWED_DOMAINS = Set.of("udla.edu.ec");

    @Nested
    @DisplayName("Factory method create")
    class FactoryMethodCreate {

        @Test
        @DisplayName("givenValidEmailAndRole_whenCreatingUser_thenSucceeds")
        void givenValidEmailAndRole_whenCreatingUser_thenSucceeds() {
            // Given
            Email email = Email.of("student@udla.edu.ec", ALLOWED_DOMAINS);
            Role role = Role.STUDENT;

            // When
            User user = User.create(email, role);

            // Then
            assertThat(user.getId()).isNotNull();
            assertThat(user.getEmail()).isEqualTo("student@udla.edu.ec");
            assertThat(user.getRole()).isEqualTo(Role.STUDENT);
            assertThat(user.isActive()).isTrue();
        }

        @Test
        @DisplayName("givenAdminRole_whenCreatingUser_thenSucceeds")
        void givenAdminRole_whenCreatingUser_thenSucceeds() {
            // Given
            Email email = Email.of("admin@udla.edu.ec", ALLOWED_DOMAINS);
            Role role = Role.ADMIN;

            // When
            User user = User.create(email, role);

            // Then
            assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        }
    }

    @Nested
    @DisplayName("Email update")
    class EmailUpdate {

        @Test
        @DisplayName("givenUser_whenUpdatingEmail_thenEmailIsUpdated")
        void givenUser_whenUpdatingEmail_thenEmailIsUpdated() {
            // Given
            Email originalEmail = Email.of("original@udla.edu.ec", ALLOWED_DOMAINS);
            User user = User.create(originalEmail, Role.STUDENT);
            Email newEmail = Email.of("updated@udla.edu.ec", ALLOWED_DOMAINS);

            // When
            user.updateEmail(newEmail);

            // Then
            assertThat(user.getEmail()).isEqualTo("updated@udla.edu.ec");
        }
    }

    @Nested
    @DisplayName("Lifecycle operations")
    class LifecycleOperations {

        @Test
        @DisplayName("givenActiveUser_whenDisabling_thenUserIsDisabled")
        void givenActiveUser_whenDisabling_thenUserIsDisabled() {
            // Given
            User user = User.create(Email.of("user@udla.edu.ec", ALLOWED_DOMAINS), Role.STUDENT);

            // When
            user.disable();

            // Then
            assertThat(user.isActive()).isFalse();
        }

        @Test
        @DisplayName("givenDisabledUser_whenDisabling_thenThrowsEntityAlreadyDisabledException")
        void givenDisabledUser_whenDisabling_thenThrowsEntityAlreadyDisabledException() {
            // Given
            User user = User.create(Email.of("user@udla.edu.ec", ALLOWED_DOMAINS), Role.STUDENT);
            user.disable();

            // When & Then
            assertThatThrownBy(user::disable)
                    .isInstanceOf(EntityAlreadyDisabledException.class);
        }

        @Test
        @DisplayName("givenDisabledUser_whenEnabling_thenUserIsEnabled")
        void givenDisabledUser_whenEnabling_thenUserIsEnabled() {
            // Given
            User user = User.create(Email.of("user@udla.edu.ec", ALLOWED_DOMAINS), Role.STUDENT);
            user.disable();

            // When
            user.enable();

            // Then
            assertThat(user.isActive()).isTrue();
        }

        @Test
        @DisplayName("givenActiveUser_whenEnabling_thenThrowsEntityAlreadyEnabledException")
        void givenActiveUser_whenEnabling_thenThrowsEntityAlreadyEnabledException() {
            // Given
            User user = User.create(Email.of("user@udla.edu.ec", ALLOWED_DOMAINS), Role.STUDENT);

            // When & Then
            assertThatThrownBy(user::enable)
                    .isInstanceOf(EntityAlreadyEnabledException.class);
        }
    }

    @Nested
    @DisplayName("Reconstruction from persistence")
    class ReconstructionFromPersistence {

        @Test
        @DisplayName("givenPersistedData_whenReconstructingUser_thenAllFieldsAreSet")
        void givenPersistedData_whenReconstructingUser_thenAllFieldsAreSet() {
            // Given
            String id = "user-attemptId-123";
            LifecycleStatus status = LifecycleStatus.ACTIVE;
            Email email = Email.of("persisted@udla.edu.ec", ALLOWED_DOMAINS);
            Role role = Role.ADMIN;

            // When
            User user = new User(id, status, email, role);

            // Then
            assertThat(user.getId()).isEqualTo(id);
            assertThat(user.getEmail()).isEqualTo("persisted@udla.edu.ec");
            assertThat(user.getRole()).isEqualTo(Role.ADMIN);
            assertThat(user.isActive()).isTrue();
        }

        @Test
        @DisplayName("givenDisabledPersistedData_whenReconstructingUser_thenUserIsDisabled")
        void givenDisabledPersistedData_whenReconstructingUser_thenUserIsDisabled() {
            // Given
            String id = "user-attemptId-456";
            LifecycleStatus status = LifecycleStatus.DISABLED;
            Email email = Email.of("disabled@udla.edu.ec", ALLOWED_DOMAINS);
            Role role = Role.STUDENT;

            // When
            User user = new User(id, status, email, role);

            // Then
            assertThat(user.isActive()).isFalse();
        }
    }
}
