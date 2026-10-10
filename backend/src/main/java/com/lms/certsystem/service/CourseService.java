package com.lms.certsystem.service;

import com.lms.certsystem.dto.CourseDTO;
import com.lms.certsystem.entity.Course;
import com.lms.certsystem.entity.Enrollment;
import com.lms.certsystem.entity.Material;
import com.lms.certsystem.entity.User;
import com.lms.certsystem.repository.CourseRepository;
import com.lms.certsystem.repository.EnrollmentRepository;
import com.lms.certsystem.repository.MaterialRepository;
import com.lms.certsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final MaterialRepository materialRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository,
                         MaterialRepository materialRepository,
                         EnrollmentRepository enrollmentRepository,
                         UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.materialRepository = materialRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public Course createCourse(CourseDTO.CreateCourseRequest request) {
        User instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found!"));

        Course course = new Course(request.getTitle(), request.getDescription(), request.getCategory(), instructor);
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public Material addMaterial(Long courseId, CourseDTO.CreateMaterialRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        Material material = new Material(
                course,
                request.getTitle(),
                request.getContentType(),
                request.getContent(),
                request.getOrderIndex()
        );
        return materialRepository.save(material);
    }

    public List<Material> getCourseMaterials(Long courseId) {
        return materialRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
    }

    public Enrollment enrollStudent(Long studentId, Long courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).get();
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        Enrollment enrollment = new Enrollment(student, course);
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public Enrollment updateProgress(Long studentId, Long courseId, Integer progress) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found!"));
        
        enrollment.setProgressPercentage(progress);
        if (progress >= 100) {
            enrollment.setStatus("COMPLETED");
        } else {
            enrollment.setStatus("IN_PROGRESS");
        }
        return enrollmentRepository.save(enrollment);
    }
}
