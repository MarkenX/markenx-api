package com.udla.markenx.api.classroom.students.domain.models.valueobjects;

import com.udla.markenx.api.classroom.students.domain.exceptions.PersonNameCannotBeEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PersonName Value Object")
class PersonNameTest {

    @Nested
    @DisplayName("Constructor validation")
    class ConstructorValidation {

        @Test
        @DisplayName("givenValidName_whenCreatingPersonName_thenSucceeds")
        void givenValidName_whenCreatingPersonName_thenSucceeds() {
            // Given
            String name = "Christian";

            // When
            PersonName personName = new PersonName(name);

            // Then
            assertThat(personName.getValue()).isEqualTo("Christian");
        }

        @Test
        @DisplayName("givenNullName_whenCreatingPersonName_thenThrowsPersonNameCannotBeEmptyException")
        void givenNullName_whenCreatingPersonName_thenThrowsPersonNameCannotBeEmptyException() {
            // Given
            String name = null;

            // When & Then
            assertThatThrownBy(() -> new PersonName(name))
                    .isInstanceOf(PersonNameCannotBeEmptyException.class);
        }

        @Test
        @DisplayName("givenBlankName_whenCreatingPersonName_thenThrowsPersonNameCannotBeEmptyException")
        void givenBlankName_whenCreatingPersonName_thenThrowsPersonNameCannotBeEmptyException() {
            // Given
            String name = "   ";

            // When & Then
            assertThatThrownBy(() -> new PersonName(name))
                    .isInstanceOf(PersonNameCannotBeEmptyException.class);
        }

        @Test
        @DisplayName("givenEmptyName_whenCreatingPersonName_thenThrowsPersonNameCannotBeEmptyException")
        void givenEmptyName_whenCreatingPersonName_thenThrowsPersonNameCannotBeEmptyException() {
            // Given
            String name = "";

            // When & Then
            assertThatThrownBy(() -> new PersonName(name))
                    .isInstanceOf(PersonNameCannotBeEmptyException.class);
        }
    }

    @Nested
    @DisplayName("Normalization")
    class Normalization {

        @Test
        @DisplayName("givenNameWithLeadingSpaces_whenCreating_thenTrimsSpaces")
        void givenNameWithLeadingSpaces_whenCreating_thenTrimsSpaces() {
            // Given
            String name = "   María";

            // When
            PersonName personName = new PersonName(name);

            // Then
            assertThat(personName.getValue()).isEqualTo("María");
        }

        @Test
        @DisplayName("givenNameWithTrailingSpaces_whenCreating_thenTrimsSpaces")
        void givenNameWithTrailingSpaces_whenCreating_thenTrimsSpaces() {
            // Given
            String name = "José   ";

            // When
            PersonName personName = new PersonName(name);

            // Then
            assertThat(personName.getValue()).isEqualTo("José");
        }

        @Test
        @DisplayName("givenNameWithMultipleInternalSpaces_whenCreating_thenNormalizesToSingleSpace")
        void givenNameWithMultipleInternalSpaces_whenCreating_thenNormalizesToSingleSpace() {
            // Given
            String name = "Juan    Carlos";

            // When
            PersonName personName = new PersonName(name);

            // Then
            assertThat(personName.getValue()).isEqualTo("Juan Carlos");
        }

        @Test
        @DisplayName("givenNameWithMixedSpaces_whenCreating_thenNormalizesCompletely")
        void givenNameWithMixedSpaces_whenCreating_thenNormalizesCompletely() {
            // Given
            String name = "  Ana    María   ";

            // When
            PersonName personName = new PersonName(name);

            // Then
            assertThat(personName.getValue()).isEqualTo("Ana María");
        }
    }

    @Nested
    @DisplayName("Change operation")
    class ChangeOperation {

        @Test
        @DisplayName("givenExistingPersonName_whenChangingToValidName_thenUpdatesValue")
        void givenExistingPersonName_whenChangingToValidName_thenUpdatesValue() {
            // Given
            PersonName personName = new PersonName("Original");

            // When
            personName.change("Updated");

            // Then
            assertThat(personName.getValue()).isEqualTo("Updated");
        }

        @Test
        @DisplayName("givenExistingPersonName_whenChangingToNull_thenThrowsPersonNameCannotBeEmptyException")
        void givenExistingPersonName_whenChangingToNull_thenThrowsPersonNameCannotBeEmptyException() {
            // Given
            PersonName personName = new PersonName("Original");

            // When & Then
            assertThatThrownBy(() -> personName.change(null))
                    .isInstanceOf(PersonNameCannotBeEmptyException.class);
        }

        @Test
        @DisplayName("givenExistingPersonName_whenChangingToBlank_thenThrowsPersonNameCannotBeEmptyException")
        void givenExistingPersonName_whenChangingToBlank_thenThrowsPersonNameCannotBeEmptyException() {
            // Given
            PersonName personName = new PersonName("Original");

            // When & Then
            assertThatThrownBy(() -> personName.change("   "))
                    .isInstanceOf(PersonNameCannotBeEmptyException.class);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoPersonNamesWithSameValue_whenComparing_thenAreEqual")
        void givenTwoPersonNamesWithSameValue_whenComparing_thenAreEqual() {
            // Given
            PersonName name1 = new PersonName("Carlos");
            PersonName name2 = new PersonName("Carlos");

            // When & Then
            assertThat(name1).isEqualTo(name2);
            assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        }

        @Test
        @DisplayName("givenTwoPersonNamesWithDifferentCase_whenComparing_thenAreEqual")
        void givenTwoPersonNamesWithDifferentCase_whenComparing_thenAreEqual() {
            // Given
            PersonName name1 = new PersonName("CARLOS");
            PersonName name2 = new PersonName("carlos");

            // When & Then
            assertThat(name1).isEqualTo(name2);
            assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        }

        @Test
        @DisplayName("givenTwoPersonNamesWithDifferentValues_whenComparing_thenAreNotEqual")
        void givenTwoPersonNamesWithDifferentValues_whenComparing_thenAreNotEqual() {
            // Given
            PersonName name1 = new PersonName("Carlos");
            PersonName name2 = new PersonName("María");

            // When & Then
            assertThat(name1).isNotEqualTo(name2);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringBehavior {

        @Test
        @DisplayName("givenPersonName_whenCallingToString_thenReturnsValue")
        void givenPersonName_whenCallingToString_thenReturnsValue() {
            // Given
            PersonName personName = new PersonName("Ana");

            // When
            String result = personName.toString();

            // Then
            assertThat(result).isEqualTo("Ana");
        }
    }
}
