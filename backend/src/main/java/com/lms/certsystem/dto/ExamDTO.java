package com.lms.certsystem.dto;

import java.util.List;

public class ExamDTO {

    public static class CreateQuestionRequest {
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String correctOption;
        private Integer marks;

        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }
        public String getOptionA() { return optionA; }
        public void setOptionA(String optionA) { this.optionA = optionA; }
        public String getOptionB() { return optionB; }
        public void setOptionB(String optionB) { this.optionB = optionB; }
        public String getOptionC() { return optionC; }
        public void setOptionC(String optionC) { this.optionC = optionC; }
        public String getOptionD() { return optionD; }
        public void setOptionD(String optionD) { this.optionD = optionD; }
        public String getCorrectOption() { return correctOption; }
        public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }
        public Integer getMarks() { return marks; }
        public void setMarks(Integer marks) { this.marks = marks; }
    }

    public static class CreateExamRequest {
        private Long courseId;
        private String title;
        private Double passMarkPercentage;
        private Integer durationMinutes;
        private List<CreateQuestionRequest> questions;

        public Long getCourseId() { return courseId; }
        public void setCourseId(Long courseId) { this.courseId = courseId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Double getPassMarkPercentage() { return passMarkPercentage; }
        public void setPassMarkPercentage(Double passMarkPercentage) { this.passMarkPercentage = passMarkPercentage; }
        public Integer getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
        public List<CreateQuestionRequest> getQuestions() { return questions; }
        public void setQuestions(List<CreateQuestionRequest> questions) { this.questions = questions; }
    }

    public static class AnswerSubmission {
        private Long questionId;
        private String selectedOption; // A, B, C, D

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getSelectedOption() { return selectedOption; }
        public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
    }

    public static class ExamSubmissionRequest {
        private Long studentId;
        private Long examId;
        private List<AnswerSubmission> answers;

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public Long getExamId() { return examId; }
        public void setExamId(Long examId) { this.examId = examId; }
        public List<AnswerSubmission> getAnswers() { return answers; }
        public void setAnswers(List<AnswerSubmission> answers) { this.answers = answers; }
    }
}
