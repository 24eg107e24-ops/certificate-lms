package com.lms.certsystem.dto;

public class AuthDTO {

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        private String role; // STUDENT, INSTRUCTOR, ADMIN

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    public static class AuthResponse {
        private Long userId;
        private String name;
        private String email;
        private String role;
        private String message;

        public AuthResponse(Long userId, String name, String email, String role, String message) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.role = role;
            this.message = message;
        }

        public Long getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getMessage() { return message; }
    }
}
