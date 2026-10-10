package com.lms.certsystem.service;

import com.lms.certsystem.dto.CourseDTO;
import com.lms.certsystem.dto.ExamDTO;
import com.lms.certsystem.entity.Course;
import com.lms.certsystem.entity.User;
import com.lms.certsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseService courseService;
    private final ExamService examService;

    public DataInitializer(UserRepository userRepository,
                           CourseService courseService,
                           ExamService examService) {
        this.userRepository = userRepository;
        this.courseService = courseService;
        this.examService = examService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("Initializing sample seed data for Certificate Management LMS...");

        // 1. Create Default Users
        User admin = new User("System Admin", "admin@lms.com", "admin123", "ADMIN");
        User instructor = new User("Dr. K. Deekshitha", "instructor@lms.com", "inst123", "INSTRUCTOR");
        User student = new User("K. Deekshitha (24EG107E24)", "student@lms.com", "stud123", "STUDENT");

        userRepository.save(admin);
        userRepository.save(instructor);
        userRepository.save(student);

        // 2. Create Sample Course
        CourseDTO.CreateCourseRequest courseReq = new CourseDTO.CreateCourseRequest();
        courseReq.setTitle("Full Stack Artificial Intelligence & Machine Learning");
        courseReq.setDescription("Master core AI/ML algorithms, neural networks, Spring Boot backend integrations, and modern full-stack application development.");
        courseReq.setCategory("AIML");
        courseReq.setInstructorId(instructor.getId());

        Course course = courseService.createCourse(courseReq);

        // 3. Add Lessons / Materials
        CourseDTO.CreateMaterialRequest mat1 = new CourseDTO.CreateMaterialRequest();
        mat1.setTitle("Module 1: Introduction to Supervised & Unsupervised Learning");
        mat1.setContentType("TEXT");
        mat1.setContent("Supervised learning uses labeled dataset to train models to classify data or predict outcomes. Unsupervised learning handles unlabeled data to discover hidden patterns.");
        mat1.setOrderIndex(1);
        courseService.addMaterial(course.getId(), mat1);

        CourseDTO.CreateMaterialRequest mat2 = new CourseDTO.CreateMaterialRequest();
        mat2.setTitle("Module 2: Spring Boot Microservices for AI Model Deployment");
        mat2.setContentType("TEXT");
        mat2.setContent("Spring Boot offers robust RESTful APIs to serve machine learning model predictions, handle user sessions, and automate certificate generation.");
        mat2.setOrderIndex(2);
        courseService.addMaterial(course.getId(), mat2);

        // 4. Create Exam with Questions
        ExamDTO.CreateExamRequest examReq = new ExamDTO.CreateExamRequest();
        examReq.setCourseId(course.getId());
        examReq.setTitle("AI/ML & Spring Boot Certification Examination");
        examReq.setPassMarkPercentage(50.0);
        examReq.setDurationMinutes(20);

        List<ExamDTO.CreateQuestionRequest> questions = new ArrayList<>();

        ExamDTO.CreateQuestionRequest q1 = new ExamDTO.CreateQuestionRequest();
        q1.setQuestionText("Which framework provides a structured backend for full-stack Java web applications?");
        q1.setOptionA("Django");
        q1.setOptionB("Spring Boot");
        q1.setOptionC("ExpressJS");
        q1.setOptionD("Flask");
        q1.setCorrectOption("B");
        q1.setMarks(2);
        questions.add(q1);

        ExamDTO.CreateQuestionRequest q2 = new ExamDTO.CreateQuestionRequest();
        q2.setQuestionText("What machine learning algorithm is commonly used for classification tasks?");
        q2.setOptionA("K-Means Clustering");
        q2.setOptionB("Linear Regression");
        q2.setOptionC("Random Forest Classifier");
        q2.setOptionD("Apriori Algorithm");
        q2.setCorrectOption("C");
        q2.setMarks(2);
        questions.add(q2);

        ExamDTO.CreateQuestionRequest q3 = new ExamDTO.CreateQuestionRequest();
        q3.setQuestionText("How does the system ensure certificate authenticity and prevent duplication?");
        q3.setOptionA("Manual signature only");
        q3.setOptionB("Unique Certificate ID & QR Code online verification");
        q3.setOptionC("Watermark only");
        q3.setOptionD("Paper printing");
        q3.setCorrectOption("B");
        q3.setMarks(2);
        questions.add(q3);

        examReq.setQuestions(questions);
        examService.createExam(examReq);

        // 5. Enroll Student
        courseService.enrollStudent(student.getId(), course.getId());

        System.out.println("Sample seed data initialization complete! Seeded 1 Admin, 1 Instructor, 1 Student, 1 Course, and 1 Exam.");
    }
}
