package com.udla.markenx.api.shared.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.exceptions.EntityAlreadyDisabledException;
import com.udla.markenx.api.shared.domain.exceptions.EntityAlreadyEnabledException;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Entity Abstract Class")
class EntityTest {

    /**
     * Concrete implementation of Entity for testing purposes.
     */
    private static class TestEntity extends Entity {
        public TestEntity() {
            super();
        }

        public TestEntity(LifecycleStatus status) {
            super(status);
        }
    }

    @Nested
    @DisplayName("Default constructor")
    class DefaultConstructor {

        @Test
        @DisplayName("givenDefaultConstructor_whenCreatingEntity_thenStatusIsActive")
        void givenDefaultConstructor_whenCreatingEntity_thenStatusIsActive() {
            // When
            TestEntity entity = new TestEntity();

            // Then
            assertThat(entity.isActive()).isTrue();
            assertThat(entity.getLifecycleStatus()).isEqualTo(LifecycleStatus.ACTIVE);
        }
    }

    @Nested
    @DisplayName("Constructor with status")
    class ConstructorWithStatus {

        @Test
        @DisplayName("givenActiveStatus_whenCreatingEntity_thenEntityIsActive")
        void givenActiveStatus_whenCreatingEntity_thenEntityIsActive() {
            // When
            TestEntity entity = new TestEntity(LifecycleStatus.ACTIVE);

            // Then
            assertThat(entity.isActive()).isTrue();
        }

        @Test
        @DisplayName("givenDisabledStatus_whenCreatingEntity_thenEntityIsDisabled")
        void givenDisabledStatus_whenCreatingEntity_thenEntityIsDisabled() {
            // When
            TestEntity entity = new TestEntity(LifecycleStatus.DISABLED);

            // Then
            assertThat(entity.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Disable operation")
    class DisableOperation {

        @Test
        @DisplayName("givenActiveEntity_whenDisabling_thenEntityIsDisabled")
        void givenActiveEntity_whenDisabling_thenEntityIsDisabled() {
            // Given
            TestEntity entity = new TestEntity();

            // When
            entity.disable();

            // Then
            assertThat(entity.isActive()).isFalse();
            assertThat(entity.getLifecycleStatus()).isEqualTo(LifecycleStatus.DISABLED);
        }

        @Test
        @DisplayName("givenDisabledEntity_whenDisabling_thenThrowsEntityAlreadyDisabledException")
        void givenDisabledEntity_whenDisabling_thenThrowsEntityAlreadyDisabledException() {
            // Given
            TestEntity entity = new TestEntity(LifecycleStatus.DISABLED);

            // When & Then
            assertThatThrownBy(entity::disable)
                    .isInstanceOf(EntityAlreadyDisabledException.class);
        }
    }

    @Nested
    @DisplayName("Enable operation")
    class EnableOperation {

        @Test
        @DisplayName("givenDisabledEntity_whenEnabling_thenEntityIsActive")
        void givenDisabledEntity_whenEnabling_thenEntityIsActive() {
            // Given
            TestEntity entity = new TestEntity(LifecycleStatus.DISABLED);

            // When
            entity.enable();

            // Then
            assertThat(entity.isActive()).isTrue();
            assertThat(entity.getLifecycleStatus()).isEqualTo(LifecycleStatus.ACTIVE);
        }

        @Test
        @DisplayName("givenActiveEntity_whenEnabling_thenThrowsEntityAlreadyEnabledException")
        void givenActiveEntity_whenEnabling_thenThrowsEntityAlreadyEnabledException() {
            // Given
            TestEntity entity = new TestEntity();

            // When & Then
            assertThatThrownBy(entity::enable)
                    .isInstanceOf(EntityAlreadyEnabledException.class);
        }
    }

    @Nested
    @DisplayName("Lifecycle transitions")
    class LifecycleTransitions {

        @Test
        @DisplayName("givenActiveEntity_whenDisablingAndEnabling_thenReturnsToActive")
        void givenActiveEntity_whenDisablingAndEnabling_thenReturnsToActive() {
            // Given
            TestEntity entity = new TestEntity();

            // When
            entity.disable();
            entity.enable();

            // Then
            assertThat(entity.isActive()).isTrue();
        }

        @Test
        @DisplayName("givenDisabledEntity_whenEnablingAndDisabling_thenReturnsToDisabled")
        void givenDisabledEntity_whenEnablingAndDisabling_thenReturnsToDisabled() {
            // Given
            TestEntity entity = new TestEntity(LifecycleStatus.DISABLED);

            // When
            entity.enable();
            entity.disable();

            // Then
            assertThat(entity.isActive()).isFalse();
        }
    }
}
