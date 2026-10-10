package com.lms.certsystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String title;

    private Double passMarkPercentage;

    private Integer durationMinutes;

    private Integer totalMarks;

    private LocalDateTime createdAt;

    public Exam() {
        this.passMarkPercentage = 50.0;
        this.durationMinutes = 30;
        this.createdAt = LocalDateTime.now();
    }

    public Exam(Course course, String title, Double passMarkPercentage, Integer durationMinutes, Integer totalMarks) {
        this.course = course;
        this.title = title;
        this.passMarkPercentage = passMarkPercentage;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getPassMarkPercentage() { return passMarkPercentage; }
    public void setPassMarkPercentage(Double passMarkPercentage) { this.passMarkPercentage = passMarkPercentage; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Integer getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Integer totalMarks) { this.totalMarks = totalMarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
