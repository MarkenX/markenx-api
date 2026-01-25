package com.udla.markenx.api.security.application.ports.out;

import com.udla.markenx.api.security.domain.models.aggregates.User;

/**
 * Puerto de comandos para persistencia de User.
 * Sigue semántica tipo JPA: save() maneja tanto inserción como actualización.
 */
public interface UserCommandRepository {

    /**
     * Guarda un usuario. Si no existe, lo inserta; si ya existe, lo actualiza.
     *
     * @param user la entidad a persistir
     * @return la entidad persistida
     */
    User save(User user);

    /**
     * Elimina (soft delete) un usuario por su ID.
     *
     * @param id el identificador del usuario
     */
    void deleteById(String id);
}
