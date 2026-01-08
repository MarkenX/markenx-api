-- turn_histories: stores the history of each turn within a game attempt
CREATE TABLE turn_histories (
    id CHAR(36) NOT NULL,
    attempt_id CHAR(36) NOT NULL,
    turn_number INT NOT NULL
        CHECK (turn_number > 0),
    acceptance_at_end DECIMAL(5,4) NOT NULL
        CHECK (acceptance_at_end >= 0 AND acceptance_at_end <= 1),
    budget_at_end DECIMAL(12,2) NOT NULL
        CHECK (budget_at_end >= 0),
    event_occurred_title VARCHAR(255) NULL,

    CONSTRAINT pk_turn_histories
        PRIMARY KEY (id),

    CONSTRAINT fk_turn_histories_attempt
        FOREIGN KEY (attempt_id)
        REFERENCES attempts (id)
        ON DELETE CASCADE,

    CONSTRAINT uk_turn_histories_attempt_turn
        UNIQUE (attempt_id, turn_number)
);

CREATE INDEX idx_turn_histories_attempt
    ON turn_histories (attempt_id);

CREATE INDEX idx_turn_histories_turn_number
    ON turn_histories (turn_number);
