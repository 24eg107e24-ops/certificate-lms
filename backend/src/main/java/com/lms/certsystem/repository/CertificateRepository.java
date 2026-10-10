package com.lms.certsystem.repository;

import com.lms.certsystem.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByCertificateId(String certificateId);
    List<Certificate> findByStudentId(Long studentId);
    Optional<Certificate> findByStudentIdAndCourseId(Long studentId, Long courseId);
    boolean existsByCertificateId(String certificateId);
}
