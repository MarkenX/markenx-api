package com.udla.markenx.api.classroom.assignments.application.ports.out;

import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import org.jspecify.annotations.NonNull;

/**
 * Command repository for StudentTaskProgress write operations.
 */
public interface StudentTaskProgressCommandRepository {

    /**
     * Saves a new student task progress record.
     *
     * @param progress The progress to save
     * @return The saved progress
     */
    StudentTaskProgress save(@NonNull StudentTaskProgress progress);

    /**
     * Updates an existing student task progress record.
     *
     * @param progress The progress to update
     * @return The updated progress
     */
    StudentTaskProgress update(@NonNull StudentTaskProgress progress);

    /**
     * Saves or updates a student task progress record.
     * Creates a new record if one doesn't exist, otherwise updates the existing one.
     *
     * @param progress The progress to save or update
     * @return The saved/updated progress
     */
    StudentTaskProgress saveOrUpdate(@NonNull StudentTaskProgress progress);
}
