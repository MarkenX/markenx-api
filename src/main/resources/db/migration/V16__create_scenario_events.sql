CREATE TABLE scenario_events (
    scenario_id CHAR(36) NOT NULL,
    event_id CHAR(36) NOT NULL,

    CONSTRAINT pk_scenario_events
        PRIMARY KEY (scenario_id, event_id),

    CONSTRAINT fk_scenario_events_scenario
        FOREIGN KEY (scenario_id)
        REFERENCES scenarios (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_scenario_events_event
        FOREIGN KEY (event_id)
        REFERENCES game_events (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_scenario_events_scenario
    ON scenario_events (scenario_id);

CREATE INDEX idx_scenario_events_event
    ON scenario_events (event_id);
