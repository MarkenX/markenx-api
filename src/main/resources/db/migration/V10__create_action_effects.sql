-- action_effects: stores effects of actions on dimensions
-- dimension_id is a UUID reference only (no FK) to support DDD module independence
CREATE TABLE action_effects (
    id CHAR(36) NOT NULL,
    action_id CHAR(36) NOT NULL,
    dimension_id CHAR(36) NOT NULL,
    delta DECIMAL(4,2) NOT NULL
        CHECK (delta >= -1 AND delta <= 1),

    CONSTRAINT pk_action_effects
        PRIMARY KEY (id),

    CONSTRAINT fk_action_effects_action
        FOREIGN KEY (action_id)
        REFERENCES actions (id)
        ON DELETE CASCADE,

    -- dimension_id: reference only (no FK) - validated at application layer

    CONSTRAINT uk_action_effects_unique
        UNIQUE (action_id, dimension_id)
);

CREATE INDEX idx_action_effects_action
    ON action_effects (action_id);

CREATE INDEX idx_action_effects_dimension
    ON action_effects (dimension_id);
