package com.lms.certsystem.service;

import com.lms.certsystem.dto.ExamDTO;
import com.lms.certsystem.entity.*;
import com.lms.certsystem.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamResultRepository examResultRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CertificateService certificateService;
    private final EnrollmentRepository enrollmentRepository;

    public ExamService(ExamRepository examRepository,
                       QuestionRepository questionRepository,
                       ExamResultRepository examResultRepository,
                       CourseRepository courseRepository,
                       UserRepository userRepository,
                       CertificateService certificateService,
                       EnrollmentRepository enrollmentRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.examResultRepository = examResultRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.certificateService = certificateService;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public Exam createExam(ExamDTO.CreateExamRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        int totalMarks = 0;
        if (request.getQuestions() != null) {
            for (ExamDTO.CreateQuestionRequest q : request.getQuestions()) {
                totalMarks += (q.getMarks() != null ? q.getMarks() : 1);
            }
        }

        Exam exam = new Exam(
                course,
                request.getTitle(),
                request.getPassMarkPercentage() != null ? request.getPassMarkPercentage() : 50.0,
                request.getDurationMinutes() != null ? request.getDurationMinutes() : 30,
                totalMarks
        );
        Exam savedExam = examRepository.save(exam);

        if (request.getQuestions() != null) {
            for (ExamDTO.CreateQuestionRequest qReq : request.getQuestions()) {
                Question question = new Question(
                        savedExam,
                        qReq.getQuestionText(),
                        qReq.getOptionA(),
                        qReq.getOptionB(),
                        qReq.getOptionC(),
                        qReq.getOptionD(),
                        qReq.getCorrectOption().toUpperCase(),
                        qReq.getMarks() != null ? qReq.getMarks() : 1
                );
                questionRepository.save(question);
            }
        }

        return savedExam;
    }

    public List<Exam> getExamsByCourse(Long courseId) {
        return examRepository.findByCourseId(courseId);
    }

    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }

    public List<Question> getQuestionsForExam(Long examId) {
        return questionRepository.findByExamId(examId);
    }

    @Transactional
    public ExamResult submitExam(ExamDTO.ExamSubmissionRequest request) {
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found!"));

        List<Question> questions = questionRepository.findByExamId(exam.getId());
        Map<Long, Question> questionMap = new HashMap<>();
        int totalPossibleMarks = 0;

        for (Question q : questions) {
            questionMap.put(q.getId(), q);
            totalPossibleMarks += q.getMarks();
        }

        int score = 0;
        if (request.getAnswers() != null) {
            for (ExamDTO.AnswerSubmission sub : request.getAnswers()) {
                Question q = questionMap.get(sub.getQuestionId());
                if (q != null && sub.getSelectedOption() != null) {
                    if (q.getCorrectOption().equalsIgnoreCase(sub.getSelectedOption().trim())) {
                        score += q.getMarks();
                    }
                }
            }
        }

        double percentage = (totalPossibleMarks > 0) ? ((double) score / totalPossibleMarks) * 100.0 : 0.0;
        String status = (percentage >= exam.getPassMarkPercentage()) ? "PASSED" : "FAILED";

        ExamResult result = new ExamResult(student, exam, score, totalPossibleMarks, percentage, status);
        ExamResult savedResult = examResultRepository.save(result);

        // If Passed: Generate Digital Certificate automatically & update course completion!
        if ("PASSED".equals(status)) {
            // Update enrollment progress to 100%
            Optional<Enrollment> enrollOpt = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), exam.getCourse().getId());
            if (enrollOpt.isPresent()) {
                Enrollment e = enrollOpt.get();
                e.setProgressPercentage(100);
                e.setStatus("COMPLETED");
                enrollmentRepository.save(e);
            }

            // Generate Certificate
            certificateService.issueCertificate(student.getId(), exam.getCourse().getId(), percentage);
        }

        return savedResult;
    }

    public List<ExamResult> getStudentResults(Long studentId) {
        return examResultRepository.findByStudentId(studentId);
    }
}
