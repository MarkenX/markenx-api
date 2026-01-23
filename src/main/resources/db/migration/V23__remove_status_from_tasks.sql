-- Remove status column from tasks table
-- Status is now tracked in student_task_progress (per student-task basis)
DROP INDEX idx_tasks_status ON tasks;

ALTER TABLE tasks
    DROP COLUMN status;
