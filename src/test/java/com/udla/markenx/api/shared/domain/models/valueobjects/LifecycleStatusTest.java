package com.udla.markenx.api.shared.domain.models.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LifecycleStatus Enum")
class LifecycleStatusTest {

    @Nested
    @DisplayName("ACTIVE status")
    class ActiveStatus {

        @Test
        @DisplayName("givenActiveStatus_whenGettingLabel_thenReturnsHabilitado")
        void givenActiveStatus_whenGettingLabel_thenReturnsHabilitado() {
            // When
            String label = LifecycleStatus.ACTIVE.getLabel();

            // Then
            assertThat(label).isEqualTo("Habilitado");
        }
    }

    @Nested
    @DisplayName("DISABLED status")
    class DisabledStatus {

        @Test
        @DisplayName("givenDisabledStatus_whenGettingLabel_thenReturnsDeshabilitado")
        void givenDisabledStatus_whenGettingLabel_thenReturnsDeshabilitado() {
            // When
            String label = LifecycleStatus.DISABLED.getLabel();

            // Then
            assertThat(label).isEqualTo("Deshabilitado");
        }
    }

    @Nested
    @DisplayName("Enum values")
    class EnumValues {

        @Test
        @DisplayName("givenLifecycleStatus_whenGettingValues_thenReturnsActiveAndDisabled")
        void givenLifecycleStatus_whenGettingValues_thenReturnsActiveAndDisabled() {
            // When
            LifecycleStatus[] values = LifecycleStatus.values();

            // Then
            assertThat(values).containsExactly(LifecycleStatus.ACTIVE, LifecycleStatus.DISABLED);
        }
    }
}
