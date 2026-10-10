package com.lms.certsystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_id"})
})
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String status; // ENROLLED, IN_PROGRESS, COMPLETED

    private Integer progressPercentage;

    private LocalDateTime enrolledAt;

    public Enrollment() {
        this.status = "ENROLLED";
        this.progressPercentage = 0;
        this.enrolledAt = LocalDateTime.now();
    }

    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
        this.status = "ENROLLED";
        this.progressPercentage = 0;
        this.enrolledAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
}
