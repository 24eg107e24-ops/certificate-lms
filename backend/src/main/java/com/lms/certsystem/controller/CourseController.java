package com.lms.certsystem.controller;

import com.lms.certsystem.dto.CourseDTO;
import com.lms.certsystem.entity.Course;
import com.lms.certsystem.entity.Enrollment;
import com.lms.certsystem.entity.Material;
import com.lms.certsystem.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseDTO.CreateCourseRequest request) {
        try {
            Course course = courseService.createCourse(request);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable Long instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }

    @PostMapping("/{courseId}/materials")
    public ResponseEntity<?> addMaterial(@PathVariable Long courseId, @RequestBody CourseDTO.CreateMaterialRequest request) {
        try {
            Material material = courseService.addMaterial(courseId, request);
            return ResponseEntity.ok(material);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{courseId}/materials")
    public ResponseEntity<List<Material>> getMaterials(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseMaterials(courseId));
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<?> enrollStudent(@PathVariable Long courseId, @RequestParam Long studentId) {
        try {
            Enrollment enrollment = courseService.enrollStudent(studentId, courseId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/enrollments")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(@PathVariable Long studentId) {
        return ResponseEntity.ok(courseService.getStudentEnrollments(studentId));
    }
}
