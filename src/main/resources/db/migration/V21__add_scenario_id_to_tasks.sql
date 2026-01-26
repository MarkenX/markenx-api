-- Add scenario_id to tasks table for Task-Scenario relationship
-- scenario_id: reference only (no FK) - validated at application layer
-- This follows the DDD module independence pattern used in attempts table

ALTER TABLE tasks
    ADD COLUMN scenario_id CHAR(36) NOT NULL;

CREATE INDEX idx_tasks_scenario
    ON tasks (scenario_id);
