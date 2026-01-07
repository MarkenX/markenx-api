CREATE TABLE scenario_dimensions (
    scenario_id CHAR(36) NOT NULL,
    dimension_id CHAR(36) NOT NULL,

    CONSTRAINT pk_scenario_dimensions
        PRIMARY KEY (scenario_id, dimension_id),

    CONSTRAINT fk_scenario_dimensions_scenario
        FOREIGN KEY (scenario_id)
        REFERENCES scenarios (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_scenario_dimensions_dimension
        FOREIGN KEY (dimension_id)
        REFERENCES dimensions (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_scenario_dimensions_scenario
    ON scenario_dimensions (scenario_id);

CREATE INDEX idx_scenario_dimensions_dimension
    ON scenario_dimensions (dimension_id);
