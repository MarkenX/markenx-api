CREATE TABLE tasks (
   id CHAR(36) NOT NULL,
   lifecycle_status VARCHAR(20) NOT NULL,
   status VARCHAR(30) NOT NULL,
   code INT NOT NULL AUTO_INCREMENT,
   title VARCHAR(255) NOT NULL,
   summary TEXT,
   deadline DATETIME NOT NULL,
   course_id CHAR(36) NOT NULL,

   min_score_to_pass DECIMAL(3,2) NOT NULL
       CHECK (min_score_to_pass >= 0 AND min_score_to_pass <= 1),
   max_attempts INT NOT NULL
       CHECK (max_attempts > 0),
   current_attempt INT NOT NULL
       CHECK (current_attempt >= 0),

   CONSTRAINT pk_tasks
       PRIMARY KEY (id),

   CONSTRAINT uk_tasks_code
       UNIQUE (code),

   CONSTRAINT fk_tasks_course
       FOREIGN KEY (course_id)
           REFERENCES courses (id)
           ON DELETE RESTRICT
           ON UPDATE CASCADE
);

CREATE INDEX idx_tasks_course
    ON tasks (course_id);

CREATE INDEX idx_tasks_status
    ON tasks (status);
