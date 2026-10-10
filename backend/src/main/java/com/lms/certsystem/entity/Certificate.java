package com.lms.certsystem.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String certificateId; // E.g., CERT-2026-8A3F9

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalDate issueDate;

    private Double scorePercentage;

    private String verificationUrl;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String qrCodeBase64;

    public Certificate() {
        this.issueDate = LocalDate.now();
    }

    public Certificate(String certificateId, User student, Course course, Double scorePercentage, String verificationUrl, String qrCodeBase64) {
        this.certificateId = certificateId;
        this.student = student;
        this.course = course;
        this.scorePercentage = scorePercentage;
        this.verificationUrl = verificationUrl;
        this.qrCodeBase64 = qrCodeBase64;
        this.issueDate = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCertificateId() { return certificateId; }
    public void setCertificateId(String certificateId) { this.certificateId = certificateId; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public Double getScorePercentage() { return scorePercentage; }
    public void setScorePercentage(Double scorePercentage) { this.scorePercentage = scorePercentage; }

    public String getVerificationUrl() { return verificationUrl; }
    public void setVerificationUrl(String verificationUrl) { this.verificationUrl = verificationUrl; }

    public String getQrCodeBase64() { return qrCodeBase64; }
    public void setQrCodeBase64(String qrCodeBase64) { this.qrCodeBase64 = qrCodeBase64; }
}
