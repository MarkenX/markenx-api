-- turn_actions: stores actions taken during each turn
-- action_id is a UUID reference only (no FK) to support DDD module independence
CREATE TABLE turn_actions (
    id CHAR(36) NOT NULL,
    turn_history_id CHAR(36) NOT NULL,
    action_id CHAR(36) NOT NULL,

    CONSTRAINT pk_turn_actions
        PRIMARY KEY (id),

    CONSTRAINT fk_turn_actions_turn_history
        FOREIGN KEY (turn_history_id)
        REFERENCES turn_histories (id)
        ON DELETE CASCADE

    -- action_id: reference only (no FK) - validated at application layer
);

CREATE INDEX idx_turn_actions_turn_history
    ON turn_actions (turn_history_id);

CREATE INDEX idx_turn_actions_action
    ON turn_actions (action_id);
