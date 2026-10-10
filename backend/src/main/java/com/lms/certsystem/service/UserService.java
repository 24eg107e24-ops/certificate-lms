package com.lms.certsystem.service;

import com.lms.certsystem.dto.AuthDTO;
import com.lms.certsystem.entity.User;
import com.lms.certsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthDTO.AuthResponse registerUser(AuthDTO.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

        String role = (request.getRole() != null && !request.getRole().isEmpty()) 
                      ? request.getRole().toUpperCase() : "STUDENT";

        User user = new User(request.getName(), request.getEmail(), request.getPassword(), role);
        User savedUser = userRepository.save(user);

        return new AuthDTO.AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                "User registered successfully!"
        );
    }

    public AuthDTO.AuthResponse loginUser(AuthDTO.LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }

        User user = userOpt.get();
        return new AuthDTO.AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                "Login successful!"
        );
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
