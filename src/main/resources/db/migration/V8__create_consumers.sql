CREATE TABLE consumers (
    id CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    age INT NULL
        CHECK (age >= 0 AND age <= 150),
    budget DECIMAL(10,2) NULL
        CHECK (budget >= 0),
    target_acceptance_score DECIMAL(3,2) NOT NULL
        CHECK (target_acceptance_score >= 0 AND target_acceptance_score <= 1),

    CONSTRAINT pk_consumers
        PRIMARY KEY (id)
);

CREATE INDEX idx_consumers_name
    ON consumers (name);

CREATE INDEX idx_consumers_target_acceptance_score
    ON consumers (target_acceptance_score);
