package com.lms.certsystem.controller;

import com.lms.certsystem.dto.ExamDTO;
import com.lms.certsystem.entity.Exam;
import com.lms.certsystem.entity.ExamResult;
import com.lms.certsystem.entity.Question;
import com.lms.certsystem.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<?> createExam(@RequestBody ExamDTO.CreateExamRequest request) {
        try {
            Exam exam = examService.createExam(request);
            return ResponseEntity.ok(exam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseId));
    }

    @GetMapping("/{examId}")
    public ResponseEntity<?> getExamById(@PathVariable Long examId) {
        return examService.getExamById(examId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{examId}/questions")
    public ResponseEntity<List<Question>> getExamQuestions(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.getQuestionsForExam(examId));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitExam(@RequestBody ExamDTO.ExamSubmissionRequest request) {
        try {
            ExamResult result = examService.submitExam(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/results")
    public ResponseEntity<List<ExamResult>> getStudentResults(@PathVariable Long studentId) {
        return ResponseEntity.ok(examService.getStudentResults(studentId));
    }
}
