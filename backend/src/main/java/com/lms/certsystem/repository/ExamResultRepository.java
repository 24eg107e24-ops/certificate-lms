package com.lms.certsystem.repository;

import com.lms.certsystem.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByStudentId(Long studentId);
    List<ExamResult> findByExamId(Long examId);
    Optional<ExamResult> findByStudentIdAndExamId(Long studentId, Long examId);
}
