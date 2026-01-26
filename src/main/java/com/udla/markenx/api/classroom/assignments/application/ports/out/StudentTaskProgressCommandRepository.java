package com.udla.markenx.api.classroom.assignments.application.ports.out;

import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import org.jspecify.annotations.NonNull;

/**
 * Puerto de comandos para persistencia de StudentTaskProgress.
 * Sigue semántica tipo JPA: save() maneja tanto inserción como actualización.
 */
public interface StudentTaskProgressCommandRepository {

    /**
     * Guarda el progreso de un estudiante en una tarea.
     * Si no existe, lo inserta; si ya existe, lo actualiza.
     *
     * @param progress el progreso a persistir
     * @return el progreso persistido
     */
    StudentTaskProgress save(@NonNull StudentTaskProgress progress);
}
