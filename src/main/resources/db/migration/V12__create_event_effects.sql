-- event_effects: stores effects of game events on dimensions
-- dimension_id is a UUID reference only (no FK) to support DDD module independence
CREATE TABLE event_effects (
    id CHAR(36) NOT NULL,
    event_id CHAR(36) NOT NULL,
    dimension_id CHAR(36) NOT NULL,
    weight_multiplier DECIMAL(5,2) NOT NULL
        CHECK (weight_multiplier >= 0),

    CONSTRAINT pk_event_effects
        PRIMARY KEY (id),

    CONSTRAINT fk_event_effects_event
        FOREIGN KEY (event_id)
        REFERENCES game_events (id)
        ON DELETE CASCADE,

    -- dimension_id: reference only (no FK) - validated at application layer

    CONSTRAINT uk_event_effects_unique
        UNIQUE (event_id, dimension_id)
);

CREATE INDEX idx_event_effects_event
    ON event_effects (event_id);

CREATE INDEX idx_event_effects_dimension
    ON event_effects (dimension_id);
