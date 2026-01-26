package com.udla.markenx.api.classroom.students.application.ports.out;

import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;

/**
 * Puerto de comandos para persistencia de Student.
 * Sigue semántica tipo JPA: save() maneja tanto inserción como actualización.
 */
public interface StudentCommandRepository {

    /**
     * Guarda un estudiante. Si no existe, lo inserta; si ya existe, lo actualiza.
     *
     * @param student la entidad a persistir
     * @return la entidad persistida
     */
    Student save(Student student);
}
