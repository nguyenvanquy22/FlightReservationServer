package com.example.flight_reservation.service.User;

import com.example.flight_reservation.dto.request.AuthRequest;
import com.example.flight_reservation.dto.response.AuthResponse;
import com.example.flight_reservation.entity.User;
import com.example.flight_reservation.entity.enums.UserRole;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.UserMapper;
import com.example.flight_reservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Đăng nhập
    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + authRequest.getEmail()));
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        // Giả lập token, trong thực tế bạn nên tạo JWT token
        String token = "dummy-jwt-token";
        AuthResponse response = new AuthResponse();
        response.setUser(userMapper.toResponse(user));
        response.setToken(token);
        return response;
    }

    // Đăng ký
    public AuthResponse register(AuthRequest authRequest) {
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        // Nếu AuthRequest không cung cấp role, bạn có thể đặt mặc định là CUSTOMER
        user.setRole(authRequest.getRole() != null ? authRequest.getRole() : UserRole.CUSTOMER);
        user = userRepository.save(user);
        // Giả lập token
        String token = "dummy-jwt-token";
        AuthResponse response = new AuthResponse();
        response.setUser(userMapper.toResponse(user));
        response.setToken(token);
        return response;
    }
}
