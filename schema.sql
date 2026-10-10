-- ============================================================
-- Certificate Management Learning Management System (LMS)
-- Database: cert_lms_db
-- Developer: K.Deekshitha (24EG107E24) AIML-E
-- Tech Stack: Spring Boot, MySQL, HTML, CSS, JavaScript
-- ============================================================

CREATE DATABASE IF NOT EXISTS `cert_lms_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `cert_lms_db`;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) NOT NULL, -- 'ADMIN', 'INSTRUCTOR', 'STUDENT'
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. Courses Table
CREATE TABLE IF NOT EXISTS `courses` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `category` VARCHAR(100),
    `instructor_id` BIGINT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_course_instructor` FOREIGN KEY (`instructor_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
);

-- 3. Materials / Lessons Table
CREATE TABLE IF NOT EXISTS `materials` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `course_id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `content_type` VARCHAR(50) DEFAULT 'TEXT',
    `content` LONGTEXT,
    `order_index` INT DEFAULT 1,
    CONSTRAINT `fk_material_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
);

-- 4. Enrollments Table
CREATE TABLE IF NOT EXISTS `enrollments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL,
    `course_id` BIGINT NOT NULL,
    `status` VARCHAR(50) DEFAULT 'ENROLLED', -- 'ENROLLED', 'IN_PROGRESS', 'COMPLETED'
    `progress_percentage` INT DEFAULT 0,
    `enrolled_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `uq_student_course` UNIQUE (`student_id`, `course_id`),
    CONSTRAINT `fk_enrollment_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_enrollment_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
);

-- 5. Exams Table
CREATE TABLE IF NOT EXISTS `exams` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `course_id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `pass_mark_percentage` DOUBLE DEFAULT 50.0,
    `duration_minutes` INT DEFAULT 30,
    `total_marks` INT DEFAULT 10,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_exam_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
);

-- 6. Questions Table
CREATE TABLE IF NOT EXISTS `questions` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `exam_id` BIGINT NOT NULL,
    `question_text` TEXT NOT NULL,
    `option_a` VARCHAR(255) NOT NULL,
    `option_b` VARCHAR(255) NOT NULL,
    `option_c` VARCHAR(255) NOT NULL,
    `option_d` VARCHAR(255) NOT NULL,
    `correct_option` CHAR(1) NOT NULL, -- 'A', 'B', 'C', 'D'
    `marks` INT DEFAULT 1,
    CONSTRAINT `fk_question_exam` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`) ON DELETE CASCADE
);

-- 7. Exam Results Table
CREATE TABLE IF NOT EXISTS `exam_results` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL,
    `exam_id` BIGINT NOT NULL,
    `score` INT NOT NULL,
    `total_marks` INT NOT NULL,
    `percentage` DOUBLE NOT NULL,
    `status` VARCHAR(50) NOT NULL, -- 'PASSED', 'FAILED'
    `completed_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_result_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_result_exam` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`) ON DELETE CASCADE
);

-- 8. Digital Certificates Table
CREATE TABLE IF NOT EXISTS `certificates` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `certificate_id` VARCHAR(100) NOT NULL UNIQUE,
    `student_id` BIGINT NOT NULL,
    `course_id` BIGINT NOT NULL,
    `issue_date` DATE NOT NULL,
    `score_percentage` DOUBLE NOT NULL,
    `verification_url` VARCHAR(500),
    `qr_code_base64` LONGTEXT,
    CONSTRAINT `fk_cert_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_cert_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
);

-- Initial Default Seed Users
INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `created_at`) VALUES
(1, 'System Admin', 'admin@lms.com', 'admin123', 'ADMIN', NOW()),
(2, 'Dr. K. Deekshitha', 'instructor@lms.com', 'inst123', 'INSTRUCTOR', NOW()),
(3, 'K. Deekshitha (24EG107E24)', 'student@lms.com', 'stud123', 'STUDENT', NOW())
ON DUPLICATE KEY UPDATE `id`=`id`;
