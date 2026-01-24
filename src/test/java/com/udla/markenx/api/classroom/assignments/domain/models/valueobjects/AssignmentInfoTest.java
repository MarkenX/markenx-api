package com.udla.markenx.api.classroom.assignments.domain.models.valueobjects;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.InvalidAssignmentDescriptionException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.InvalidAssignmentTitleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AssignmentInfo Value Object")
class AssignmentInfoTest {

    @Nested
    @DisplayName("Constructor validation")
    class ConstructorValidation {

        @Test
        @DisplayName("givenValidTitleAndSummary_whenCreatingAssignmentInfo_thenSucceeds")
        void givenValidTitleAndSummary_whenCreatingAssignmentInfo_thenSucceeds() {
            // Given
            String title = "Marketing Strategy Analysis";
            String summary = "Analyze the marketing strategy of a chosen company";

            // When
            AssignmentInfo info = new AssignmentInfo(title, summary);

            // Then
            assertThat(info.title()).isEqualTo(title);
            assertThat(info.summary()).isEqualTo(summary);
        }

        @Test
        @DisplayName("givenNullTitle_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentTitleException")
        void givenNullTitle_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentTitleException() {
            // Given
            String title = null;
            String summary = "Valid summary";

            // When & Then
            assertThatThrownBy(() -> new AssignmentInfo(title, summary))
                    .isInstanceOf(InvalidAssignmentTitleException.class);
        }

        @Test
        @DisplayName("givenBlankTitle_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentTitleException")
        void givenBlankTitle_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentTitleException() {
            // Given
            String title = "   ";
            String summary = "Valid summary";

            // When & Then
            assertThatThrownBy(() -> new AssignmentInfo(title, summary))
                    .isInstanceOf(InvalidAssignmentTitleException.class);
        }

        @Test
        @DisplayName("givenEmptyTitle_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentTitleException")
        void givenEmptyTitle_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentTitleException() {
            // Given
            String title = "";
            String summary = "Valid summary";

            // When & Then
            assertThatThrownBy(() -> new AssignmentInfo(title, summary))
                    .isInstanceOf(InvalidAssignmentTitleException.class);
        }

        @Test
        @DisplayName("givenNullSummary_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentDescriptionException")
        void givenNullSummary_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentDescriptionException() {
            // Given
            String title = "Valid title";
            String summary = null;

            // When & Then
            assertThatThrownBy(() -> new AssignmentInfo(title, summary))
                    .isInstanceOf(InvalidAssignmentDescriptionException.class);
        }

        @Test
        @DisplayName("givenBlankSummary_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentDescriptionException")
        void givenBlankSummary_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentDescriptionException() {
            // Given
            String title = "Valid title";
            String summary = "   ";

            // When & Then
            assertThatThrownBy(() -> new AssignmentInfo(title, summary))
                    .isInstanceOf(InvalidAssignmentDescriptionException.class);
        }

        @Test
        @DisplayName("givenEmptySummary_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentDescriptionException")
        void givenEmptySummary_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentDescriptionException() {
            // Given
            String title = "Valid title";
            String summary = "";

            // When & Then
            assertThatThrownBy(() -> new AssignmentInfo(title, summary))
                    .isInstanceOf(InvalidAssignmentDescriptionException.class);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("givenTwoAssignmentInfoWithSameValues_whenComparing_thenAreEqual")
        void givenTwoAssignmentInfoWithSameValues_whenComparing_thenAreEqual() {
            // Given
            AssignmentInfo info1 = new AssignmentInfo("Title", "Summary");
            AssignmentInfo info2 = new AssignmentInfo("Title", "Summary");

            // When & Then
            assertThat(info1).isEqualTo(info2);
            assertThat(info1.hashCode()).isEqualTo(info2.hashCode());
        }

        @Test
        @DisplayName("givenTwoAssignmentInfoWithDifferentTitles_whenComparing_thenAreNotEqual")
        void givenTwoAssignmentInfoWithDifferentTitles_whenComparing_thenAreNotEqual() {
            // Given
            AssignmentInfo info1 = new AssignmentInfo("Title 1", "Summary");
            AssignmentInfo info2 = new AssignmentInfo("Title 2", "Summary");

            // When & Then
            assertThat(info1).isNotEqualTo(info2);
        }

        @Test
        @DisplayName("givenTwoAssignmentInfoWithDifferentSummaries_whenComparing_thenAreNotEqual")
        void givenTwoAssignmentInfoWithDifferentSummaries_whenComparing_thenAreNotEqual() {
            // Given
            AssignmentInfo info1 = new AssignmentInfo("Title", "Summary 1");
            AssignmentInfo info2 = new AssignmentInfo("Title", "Summary 2");

            // When & Then
            assertThat(info1).isNotEqualTo(info2);
        }
    }
}
