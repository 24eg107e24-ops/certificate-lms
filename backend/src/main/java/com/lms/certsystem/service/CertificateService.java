package com.lms.certsystem.service;

import com.lms.certsystem.entity.Certificate;
import com.lms.certsystem.entity.Course;
import com.lms.certsystem.entity.User;
import com.lms.certsystem.repository.CertificateRepository;
import com.lms.certsystem.repository.CourseRepository;
import com.lms.certsystem.repository.UserRepository;
import com.lms.certsystem.util.PDFCertificateGenerator;
import com.lms.certsystem.util.QRCodeGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public CertificateService(CertificateRepository certificateRepository,
                              UserRepository userRepository,
                              CourseRepository courseRepository) {
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public Certificate issueCertificate(Long studentId, Long courseId, Double scorePercentage) {
        // Check if certificate already exists for this student and course
        Optional<Certificate> existing = certificateRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (existing.isPresent()) {
            return existing.get();
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        // Generate unique certificate ID
        String certId = "CERT-2026-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Verification URL for QR Code
        String verifyUrl = "http://localhost:8080/api/certificates/verify/" + certId;

        // Generate QR code base64 image
        String qrBase64 = QRCodeGenerator.generateQRCodeBase64(verifyUrl, 150, 150);

        Certificate cert = new Certificate(certId, student, course, scorePercentage, verifyUrl, qrBase64);
        return certificateRepository.save(cert);
    }

    public List<Certificate> getStudentCertificates(Long studentId) {
        return certificateRepository.findByStudentId(studentId);
    }

    public Optional<Certificate> verifyCertificate(String certificateId) {
        return certificateRepository.findByCertificateId(certificateId.trim());
    }

    public byte[] getCertificatePdf(String certificateId) {
        Certificate cert = certificateRepository.findByCertificateId(certificateId.trim())
                .orElseThrow(() -> new RuntimeException("Certificate not found for ID: " + certificateId));

        return PDFCertificateGenerator.generateCertificatePdf(cert);
    }

    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }
}
