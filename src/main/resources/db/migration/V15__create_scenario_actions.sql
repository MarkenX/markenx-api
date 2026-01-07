CREATE TABLE scenario_actions (
    scenario_id CHAR(36) NOT NULL,
    action_id CHAR(36) NOT NULL,

    CONSTRAINT pk_scenario_actions
        PRIMARY KEY (scenario_id, action_id),

    CONSTRAINT fk_scenario_actions_scenario
        FOREIGN KEY (scenario_id)
        REFERENCES scenarios (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_scenario_actions_action
        FOREIGN KEY (action_id)
        REFERENCES actions (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_scenario_actions_scenario
    ON scenario_actions (scenario_id);

CREATE INDEX idx_scenario_actions_action
    ON scenario_actions (action_id);
