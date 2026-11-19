CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- Tables for the Student Profile and Skill Development Management System

CREATE TABLE IF NOT EXISTS students (
    student_id        INT           NOT NULL,                 -- supplied by the user, PRIMARY KEY => no duplicate IDs
    student_name      VARCHAR(100)  NOT NULL,
    email             VARCHAR(150)  NOT NULL,
    course_name       VARCHAR(100)  NOT NULL,
    programming_score DECIMAL(5,2)  NOT NULL DEFAULT 0,
    mathematics_score DECIMAL(5,2)  NOT NULL DEFAULT 0,
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id),
    CONSTRAINT uq_students_email UNIQUE (email),
    CONSTRAINT chk_programming_score CHECK (programming_score BETWEEN 0 AND 100),
    CONSTRAINT chk_mathematics_score CHECK (mathematics_score BETWEEN 0 AND 100)
);

-- One row per (student, skill): one-to-many relationship
CREATE TABLE IF NOT EXISTS student_skills (
    student_id INT         NOT NULL,
    skill_name VARCHAR(60) NOT NULL,
    PRIMARY KEY (student_id, skill_name),
    CONSTRAINT fk_skills_student FOREIGN KEY (student_id)
        REFERENCES students (student_id) ON DELETE CASCADE
);

-- Sample data (INSERT IGNORE => safe to run on every startup)
INSERT IGNORE INTO students (student_id, student_name, email, course_name, programming_score, mathematics_score) VALUES
 (101, 'Riya Sharma',  'riya@example.com',  'B.Tech CSE',     88.50, 92.00),
 (102, 'Arjun Verma',  'arjun@example.com', 'B.Tech CSE',     75.00, 68.00),
 (103, 'Neha Gupta',   'neha@example.com',  'B.Tech IT',      45.00, 82.00),
 (104, 'Karan Singh',  'karan@example.com', 'BCA',            62.00, 58.50),
 (105, 'Sneha Yadav',  'sneha@example.com', 'B.Tech CSE-AI',  40.00, 35.00),
 (106, 'Aman Mishra',  'aman@example.com',  'B.Tech IT',      30.00, 78.00),
 (107, 'Pooja Rao',    'pooja@example.com', 'B.Tech CSE',     91.00, 85.00);

INSERT IGNORE INTO student_skills (student_id, skill_name) VALUES
 (101, 'Java'), (101, 'SQL'), (101, 'Git'),
 (102, 'Python'), (102, 'SQL'),
 (103, 'Java'), (103, 'SQL'), (103, 'Git'),
 (104, 'HTML'), (104, 'CSS'),
 (105, 'Python'), (105, 'SQL'),
 (106, 'HTML'), (106, 'CSS'),
 (107, 'Java'), (107, 'Spring Boot');
