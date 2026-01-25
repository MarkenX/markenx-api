package com.udla.markenx.api.classroom.assignments.application.ports.out;

/**
 * Anti-Corruption Layer port for validating scenario existence from the game module.
 * This port decouples the assignments module from the game/scenarios module,
 * following DDD bounded context principles.
 */
public interface ScenarioValidationPort {

    /**
     * Checks if a scenario exists by its ID.
     *
     * @param scenarioId the scenario ID to validate
     * @return true if the scenario exists, false otherwise
     */
    boolean existsById(String scenarioId);
}
