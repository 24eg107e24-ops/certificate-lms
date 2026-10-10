package com.lms.certsystem.controller;

import com.lms.certsystem.entity.User;
import com.lms.certsystem.service.CertificateService;
import com.lms.certsystem.service.CourseService;
import com.lms.certsystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;
    private final CertificateService certificateService;

    public AdminController(UserService userService, CourseService courseService, CertificateService certificateService) {
        this.userService = userService;
        this.courseService = courseService;
        this.certificateService = certificateService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getAllUsers().size());
        stats.put("totalCourses", courseService.getAllCourses().size());
        stats.put("totalCertificates", certificateService.getAllCertificates().size());
        return ResponseEntity.ok(stats);
    }
}
