-- Add status column to student_task_progress
-- Status is now tracked per student-task combination instead of per task
ALTER TABLE student_task_progress
    ADD COLUMN status VARCHAR(30) NOT NULL DEFAULT 'NOT_STARTED';

CREATE INDEX idx_student_task_progress_status
    ON student_task_progress (status);
