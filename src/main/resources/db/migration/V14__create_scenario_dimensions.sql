-- scenario_dimensions: associates dimensions with scenarios
-- dimension_id is a UUID reference only (no FK) to support DDD module independence
CREATE TABLE scenario_dimensions (
    scenario_id CHAR(36) NOT NULL,
    dimension_id CHAR(36) NOT NULL,

    CONSTRAINT pk_scenario_dimensions
        PRIMARY KEY (scenario_id, dimension_id),

    CONSTRAINT fk_scenario_dimensions_scenario
        FOREIGN KEY (scenario_id)
        REFERENCES scenarios (id)
        ON DELETE CASCADE

    -- dimension_id: reference only (no FK) - validated at application layer
);

CREATE INDEX idx_scenario_dimensions_scenario
    ON scenario_dimensions (scenario_id);

CREATE INDEX idx_scenario_dimensions_dimension
    ON scenario_dimensions (dimension_id);
