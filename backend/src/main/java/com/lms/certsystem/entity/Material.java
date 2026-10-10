package com.lms.certsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "materials")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String title;

    private String contentType; // TEXT, LINK, DOCUMENT

    @Column(length = 4000)
    private String content;

    private Integer orderIndex;

    public Material() {}

    public Material(Course course, String title, String contentType, String content, Integer orderIndex) {
        this.course = course;
        this.title = title;
        this.contentType = contentType;
        this.content = content;
        this.orderIndex = orderIndex;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}
