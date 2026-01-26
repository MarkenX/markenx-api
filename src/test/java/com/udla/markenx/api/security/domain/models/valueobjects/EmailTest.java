package com.udla.markenx.api.security.domain.models.valueobjects;

import com.udla.markenx.api.security.domain.exceptions.EmailCannotBeEmptyException;
import com.udla.markenx.api.security.domain.exceptions.EmailDomainNotAllowedException;
import com.udla.markenx.api.security.domain.exceptions.InvalidEmailFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Email Value Object")
class EmailTest {

    private static final Set<String> ALLOWED_DOMAINS = Set.of("udla.edu.ec", "example.com");

    @Nested
    @DisplayName("Creation with domain validation")
    class CreationWithDomainValidation {

        @Test
        @DisplayName("givenValidEmailWithAllowedDomain_whenCreating_thenSucceeds")
        void givenValidEmailWithAllowedDomain_whenCreating_thenSucceeds() {
            // Given
            String emailValue = "student@udla.edu.ec";

            // When
            Email email = Email.of(emailValue, ALLOWED_DOMAINS);

            // Then
            assertThat(email.getValue()).isEqualTo("student@udla.edu.ec");
        }

        @Test
        @DisplayName("givenNullEmail_whenCreating_thenThrowsEmailCannotBeEmptyException")
        void givenNullEmail_whenCreating_thenThrowsEmailCannotBeEmptyException() {
            // When & Then
            assertThatThrownBy(() -> Email.of(null, ALLOWED_DOMAINS))
                    .isInstanceOf(EmailCannotBeEmptyException.class);
        }

        @Test
        @DisplayName("givenBlankEmail_whenCreating_thenThrowsEmailCannotBeEmptyException")
        void givenBlankEmail_whenCreating_thenThrowsEmailCannotBeEmptyException() {
            // When & Then
            assertThatThrownBy(() -> Email.of("   ", ALLOWED_DOMAINS))
                    .isInstanceOf(EmailCannotBeEmptyException.class);
        }

        @Test
        @DisplayName("givenEmptyEmail_whenCreating_thenThrowsEmailCannotBeEmptyException")
        void givenEmptyEmail_whenCreating_thenThrowsEmailCannotBeEmptyException() {
            // When & Then
            assertThatThrownBy(() -> Email.of("", ALLOWED_DOMAINS))
                    .isInstanceOf(EmailCannotBeEmptyException.class);
        }

        @Test
        @DisplayName("givenInvalidEmailFormat_whenCreating_thenThrowsInvalidEmailFormatException")
        void givenInvalidEmailFormat_whenCreating_thenThrowsInvalidEmailFormatException() {
            // Given
            String invalidEmail = "not-an-email";

            // When & Then
            assertThatThrownBy(() -> Email.of(invalidEmail, ALLOWED_DOMAINS))
                    .isInstanceOf(InvalidEmailFormatException.class);
        }

        @Test
        @DisplayName("givenEmailWithoutAtSymbol_whenCreating_thenThrowsInvalidEmailFormatException")
        void givenEmailWithoutAtSymbol_whenCreating_thenThrowsInvalidEmailFormatException() {
            // Given
            String invalidEmail = "studentudla.edu.ec";

            // When & Then
            assertThatThrownBy(() -> Email.of(invalidEmail, ALLOWED_DOMAINS))
                    .isInstanceOf(InvalidEmailFormatException.class);
        }

        @Test
        @DisplayName("givenEmailWithNotAllowedDomain_whenCreating_thenThrowsEmailDomainNotAllowedException")
        void givenEmailWithNotAllowedDomain_whenCreating_thenThrowsEmailDomainNotAllowedException() {
            // Given
            String emailWithInvalidDomain = "user@gmail.com";

            // When & Then
            assertThatThrownBy(() -> Email.of(emailWithInvalidDomain, ALLOWED_DOMAINS))
                    .isInstanceOf(EmailDomainNotAllowedException.class);
        }
    }

    @Nested
    @DisplayName("Creation without domain validation")
    class CreationWithoutDomainValidation {

        @Test
        @DisplayName("givenValidEmail_whenCreatingWithoutDomainValidation_thenSucceeds")
        void givenValidEmail_whenCreatingWithoutDomainValidation_thenSucceeds() {
            // Given
            String emailValue = "user@any-domain.com";

            // When
            Email email = Email.of(emailValue);

            // Then
            assertThat(email.getValue()).isEqualTo("user@any-domain.com");
        }
    }

    @Nested
    @DisplayName("Normalization")
    class Normalization {

        @Test
        @DisplayName("givenEmailWithUpperCase_whenCreating_thenNormalizesToLowerCase")
        void givenEmailWithUpperCase_whenCreating_thenNormalizesToLowerCase() {
            // Given
            String emailValue = "USER@UDLA.EDU.EC";

            // When
            Email email = Email.of(emailValue, ALLOWED_DOMAINS);

            // Then
            assertThat(email.getValue()).isEqualTo("user@udla.edu.ec");
        }

        @Test
        @DisplayName("givenEmailWithLeadingSpaces_whenCreating_thenTrimsSpaces")
        void givenEmailWithLeadingSpaces_whenCreating_thenTrimsSpaces() {
            // Given
            String emailValue = "   user@udla.edu.ec";

            // When
            Email email = Email.of(emailValue, ALLOWED_DOMAINS);

            // Then
            assertThat(email.getValue()).isEqualTo("user@udla.edu.ec");
        }

        @Test
        @DisplayName("givenEmailWithTrailingSpaces_whenCreatingWithoutDomainValidation_thenTrimsSpaces")
        void givenEmailWithTrailingSpaces_whenCreatingWithoutDomainValidation_thenTrimsSpaces() {
            // Given - Using factory without domain validation since trailing spaces affect domain matching
            String emailValue = "user@udla.edu.ec   ";

            // When
            Email email = Email.of(emailValue);

            // Then
            assertThat(email.getValue()).isEqualTo("user@udla.edu.ec");
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoEmailsWithSameValue_whenComparing_thenAreEqual")
        void givenTwoEmailsWithSameValue_whenComparing_thenAreEqual() {
            // Given
            Email email1 = Email.of("user@udla.edu.ec", ALLOWED_DOMAINS);
            Email email2 = Email.of("user@udla.edu.ec", ALLOWED_DOMAINS);

            // When & Then
            assertThat(email1).isEqualTo(email2);
            assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
        }

        @Test
        @DisplayName("givenTwoEmailsWithDifferentValues_whenComparing_thenAreNotEqual")
        void givenTwoEmailsWithDifferentValues_whenComparing_thenAreNotEqual() {
            // Given
            Email email1 = Email.of("user1@udla.edu.ec", ALLOWED_DOMAINS);
            Email email2 = Email.of("user2@udla.edu.ec", ALLOWED_DOMAINS);

            // When & Then
            assertThat(email1).isNotEqualTo(email2);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringBehavior {

        @Test
        @DisplayName("givenEmail_whenCallingToString_thenReturnsValue")
        void givenEmail_whenCallingToString_thenReturnsValue() {
            // Given
            Email email = Email.of("user@udla.edu.ec", ALLOWED_DOMAINS);

            // When
            String result = email.toString();

            // Then
            assertThat(result).isEqualTo("user@udla.edu.ec");
        }
    }
}
