CREATE TABLE actions (
    id CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    cost DECIMAL(10,2) NOT NULL
        CHECK (cost >= 0),
    category VARCHAR(20) NOT NULL,
    is_initially_locked BOOLEAN NOT NULL DEFAULT FALSE,
    prerequisite_action_id CHAR(36) NULL,

    CONSTRAINT pk_actions
        PRIMARY KEY (id),

    CONSTRAINT uk_actions_name
        UNIQUE (name),

    CONSTRAINT fk_actions_prerequisite
        FOREIGN KEY (prerequisite_action_id)
        REFERENCES actions (id)
        ON DELETE SET NULL
);

CREATE INDEX idx_actions_category
    ON actions (category);

CREATE INDEX idx_actions_prerequisite
    ON actions (prerequisite_action_id);

CREATE INDEX idx_actions_cost
    ON actions (cost);
