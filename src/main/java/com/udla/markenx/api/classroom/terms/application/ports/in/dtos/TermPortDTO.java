package com.udla.markenx.api.classroom.terms.application.ports.in.dtos;

import com.udla.markenx.api.classroom.terms.domain.models.valueobjects.TermStatus;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.util.Optional;

public record TermPortDTO(
        String id,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        String statusLabel,
        String label
) {
    public boolean isActive() {
        return statusEnum().orElse(null) == TermStatus.ACTIVE;
    }

    public boolean isUpcoming() {
        return statusEnum().orElse(null) == TermStatus.UPCOMING;
    }

    public boolean hasEnded() {
        return statusEnum().orElse(null) == TermStatus.ENDED;
    }

    private Optional<TermStatus> statusEnum() {
        if (status == null || status.isBlank()) return Optional.empty();

        try {
            return Optional.of(TermStatus.valueOf(status));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    @Override
    public @NonNull String toString() {
        return "Term-%s (attemptId=%s, %s..%s, status=%s)"
                .formatted(label, id, startDate, endDate, status);
    }
}

