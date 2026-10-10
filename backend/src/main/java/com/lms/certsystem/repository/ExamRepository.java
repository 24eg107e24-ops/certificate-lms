package com.lms.certsystem.repository;

import com.lms.certsystem.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourseId(Long courseId);
    Optional<Exam> findFirstByCourseId(Long courseId);
}
