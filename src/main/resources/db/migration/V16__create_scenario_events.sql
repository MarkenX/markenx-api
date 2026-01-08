-- scenario_events: associates game events with scenarios
-- event_id is a UUID reference only (no FK) to support DDD module independence
CREATE TABLE scenario_events (
    scenario_id CHAR(36) NOT NULL,
    event_id CHAR(36) NOT NULL,

    CONSTRAINT pk_scenario_events
        PRIMARY KEY (scenario_id, event_id),

    CONSTRAINT fk_scenario_events_scenario
        FOREIGN KEY (scenario_id)
        REFERENCES scenarios (id)
        ON DELETE CASCADE

    -- event_id: reference only (no FK) - validated at application layer
);

CREATE INDEX idx_scenario_events_scenario
    ON scenario_events (scenario_id);

CREATE INDEX idx_scenario_events_event
    ON scenario_events (event_id);
