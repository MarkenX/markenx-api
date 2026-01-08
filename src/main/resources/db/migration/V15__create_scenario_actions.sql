-- scenario_actions: associates actions with scenarios
-- action_id is a UUID reference only (no FK) to support DDD module independence
CREATE TABLE scenario_actions (
    scenario_id CHAR(36) NOT NULL,
    action_id CHAR(36) NOT NULL,

    CONSTRAINT pk_scenario_actions
        PRIMARY KEY (scenario_id, action_id),

    CONSTRAINT fk_scenario_actions_scenario
        FOREIGN KEY (scenario_id)
        REFERENCES scenarios (id)
        ON DELETE CASCADE

    -- action_id: reference only (no FK) - validated at application layer
);

CREATE INDEX idx_scenario_actions_scenario
    ON scenario_actions (scenario_id);

CREATE INDEX idx_scenario_actions_action
    ON scenario_actions (action_id);
