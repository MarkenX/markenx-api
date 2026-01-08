-- attempts: stores game session results for student task attempts
-- References task_id and student_id as UUIDs (no FK) for DDD module independence
CREATE TABLE attempts (
    id CHAR(36) NOT NULL,
    task_id CHAR(36) NOT NULL,
    student_id CHAR(36) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN',
    session_date TIMESTAMP NOT NULL,
    final_acceptance DECIMAL(5,4) NOT NULL
        CHECK (final_acceptance >= 0 AND final_acceptance <= 1),
    remaining_budget DECIMAL(12,2) NOT NULL
        CHECK (remaining_budget >= 0),
    total_turns_used INT NOT NULL
        CHECK (total_turns_used > 0),
    profile_discovery_percentage DECIMAL(5,4) NOT NULL
        CHECK (profile_discovery_percentage >= 0 AND profile_discovery_percentage <= 1),
    final_outcome VARCHAR(20) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_attempts
        PRIMARY KEY (id),

    CONSTRAINT chk_attempts_status
        CHECK (status IN ('UNKNOWN', 'APPROVED', 'DISAPPROVED')),

    CONSTRAINT chk_attempts_outcome
        CHECK (final_outcome IS NULL OR final_outcome IN ('APPROVED', 'DISAPPROVED'))

    -- task_id: reference only (no FK) - validated at application layer
    -- student_id: reference only (no FK) - validated at application layer
);

CREATE INDEX idx_attempts_task
    ON attempts (task_id);

CREATE INDEX idx_attempts_student
    ON attempts (student_id);

CREATE INDEX idx_attempts_status
    ON attempts (status);

CREATE INDEX idx_attempts_session_date
    ON attempts (session_date);
