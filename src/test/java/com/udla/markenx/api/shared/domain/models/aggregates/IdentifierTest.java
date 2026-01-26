package com.udla.markenx.api.shared.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.exceptions.IdentifierCannotBeNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Identifier Abstract Class")
class IdentifierTest {

    /**
     * Concrete implementation of Identifier for testing purposes.
     */
    private static class TestIdentifier extends Identifier {
        protected TestIdentifier(String value) {
            super(value);
        }
    }

    @Nested
    @DisplayName("Constructor validation")
    class ConstructorValidation {

        @Test
        @DisplayName("givenValidValue_whenCreatingIdentifier_thenSucceeds")
        void givenValidValue_whenCreatingIdentifier_thenSucceeds() {
            // Given
            String value = "valid-attemptId-123";

            // When
            TestIdentifier identifier = new TestIdentifier(value);

            // Then
            assertThat(identifier.value()).isEqualTo(value);
        }

        @Test
        @DisplayName("givenNullValue_whenCreatingIdentifier_thenThrowsIdentifierCannotBeNullException")
        void givenNullValue_whenCreatingIdentifier_thenThrowsIdentifierCannotBeNullException() {
            // When & Then
            assertThatThrownBy(() -> new TestIdentifier(null))
                    .isInstanceOf(IdentifierCannotBeNullException.class);
        }

        @Test
        @DisplayName("givenBlankValue_whenCreatingIdentifier_thenThrowsIdentifierCannotBeNullException")
        void givenBlankValue_whenCreatingIdentifier_thenThrowsIdentifierCannotBeNullException() {
            // When & Then
            assertThatThrownBy(() -> new TestIdentifier("   "))
                    .isInstanceOf(IdentifierCannotBeNullException.class);
        }

        @Test
        @DisplayName("givenEmptyValue_whenCreatingIdentifier_thenThrowsIdentifierCannotBeNullException")
        void givenEmptyValue_whenCreatingIdentifier_thenThrowsIdentifierCannotBeNullException() {
            // When & Then
            assertThatThrownBy(() -> new TestIdentifier(""))
                    .isInstanceOf(IdentifierCannotBeNullException.class);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoIdentifiersWithSameValue_whenComparing_thenAreEqual")
        void givenTwoIdentifiersWithSameValue_whenComparing_thenAreEqual() {
            // Given
            TestIdentifier id1 = new TestIdentifier("same-value");
            TestIdentifier id2 = new TestIdentifier("same-value");

            // When & Then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("givenTwoIdentifiersWithDifferentValues_whenComparing_thenAreNotEqual")
        void givenTwoIdentifiersWithDifferentValues_whenComparing_thenAreNotEqual() {
            // Given
            TestIdentifier id1 = new TestIdentifier("value-1");
            TestIdentifier id2 = new TestIdentifier("value-2");

            // When & Then
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        @DisplayName("givenIdentifierAndNull_whenComparing_thenAreNotEqual")
        void givenIdentifierAndNull_whenComparing_thenAreNotEqual() {
            // Given
            TestIdentifier id = new TestIdentifier("value");

            // When & Then
            assertThat(id).isNotEqualTo(null);
        }

        @Test
        @DisplayName("givenSameIdentifierInstance_whenComparing_thenAreEqual")
        void givenSameIdentifierInstance_whenComparing_thenAreEqual() {
            // Given
            TestIdentifier id = new TestIdentifier("value");

            // When & Then
            assertThat(id).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringBehavior {

        @Test
        @DisplayName("givenIdentifier_whenCallingToString_thenReturnsValue")
        void givenIdentifier_whenCallingToString_thenReturnsValue() {
            // Given
            String value = "my-unique-attemptId";
            TestIdentifier identifier = new TestIdentifier(value);

            // When
            String result = identifier.toString();

            // Then
            assertThat(result).isEqualTo(value);
        }
    }
}
