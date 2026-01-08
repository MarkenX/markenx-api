-- scenarios: main game scenarios for marketing simulation
-- consumer_id is a UUID reference only (no FK) to support DDD module independence
CREATE TABLE scenarios (
    id CHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    consumer_id CHAR(36) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_scenarios
        PRIMARY KEY (id),

    CONSTRAINT uk_scenarios_title
        UNIQUE (title)

    -- consumer_id: reference only (no FK) - validated at application layer
);

CREATE INDEX idx_scenarios_title
    ON scenarios (title);

CREATE INDEX idx_scenarios_consumer
    ON scenarios (consumer_id);

CREATE INDEX idx_scenarios_created_at
    ON scenarios (created_at);
