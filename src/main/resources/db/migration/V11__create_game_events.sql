CREATE TABLE game_events (
    id CHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,

    CONSTRAINT pk_game_events
        PRIMARY KEY (id),

    CONSTRAINT uk_game_events_title
        UNIQUE (title)
);

CREATE INDEX idx_game_events_title
    ON game_events (title);
