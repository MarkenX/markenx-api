-- student_task_progress: tracks student progress on tasks (current attempt number)
-- References student_id and task_id as UUIDs (no FK) for DDD module independence
CREATE TABLE student_task_progress (
    student_id CHAR(36) NOT NULL,
    task_id CHAR(36) NOT NULL,
    current_attempt INT NOT NULL DEFAULT 0
        CHECK (current_attempt >= 0),

    CONSTRAINT pk_student_task_progress
        PRIMARY KEY (student_id, task_id)

    -- student_id: reference only (no FK) - validated at application layer
    -- task_id: reference only (no FK) - validated at application layer
);

CREATE INDEX idx_student_task_progress_student
    ON student_task_progress (student_id);

CREATE INDEX idx_student_task_progress_task
    ON student_task_progress (task_id);
