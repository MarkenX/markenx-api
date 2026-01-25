package com.udla.markenx.api.classroom.assignments.application.ports.out;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;

/**
 * Puerto de comandos para persistencia de Task.
 * Sigue semántica tipo JPA: save() maneja tanto inserción como actualización.
 */
public interface TaskCommandRepository {

    /**
     * Guarda una tarea. Si no existe, la inserta; si ya existe, la actualiza.
     *
     * @param task la entidad a persistir
     * @return la entidad persistida
     */
    Task save(Task task);
}
