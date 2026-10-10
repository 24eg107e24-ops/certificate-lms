package com.lms.certsystem.dto;

public class CourseDTO {

    public static class CreateCourseRequest {
        private String title;
        private String description;
        private String category;
        private Long instructorId;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public Long getInstructorId() { return instructorId; }
        public void setInstructorId(Long instructorId) { this.instructorId = instructorId; }
    }

    public static class CreateMaterialRequest {
        private String title;
        private String contentType;
        private String content;
        private Integer orderIndex;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Integer getOrderIndex() { return orderIndex; }
        public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    }
}
