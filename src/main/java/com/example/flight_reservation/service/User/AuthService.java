package com.example.flight_reservation.service.User;

import com.example.flight_reservation.dto.request.AuthRequest;
import com.example.flight_reservation.dto.response.AuthResponse;
import com.example.flight_reservation.entity.User;
import com.example.flight_reservation.entity.enums.UserRole;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.UserMapper;
import com.example.flight_reservation.repository.UserRepository;
import com.example.flight_reservation.service.JWT.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired private UserRepository userRepository;
    @Autowired private UserMapper userMapper;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JWTService jwtService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserDetailsService userDetailsService;

    // Đăng nhập
    public AuthResponse login(AuthRequest req) {
        // 1. Xác thực credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        // 2. Load lại thông tin user
        User u = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + req.getEmail()));

        // 3. Sinh JWT token
        UserDetails ud = userDetailsService.loadUserByUsername(u.getEmail());
        String token = jwtService.generateToken(ud);

        AuthResponse resp = new AuthResponse();
        resp.setUser(userMapper.toResponse(u));
        resp.setToken(token);
        return resp;
    }

    // Đăng ký
    public AuthResponse register(AuthRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User u = new User();
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(req.getRole() != null ? req.getRole() : UserRole.CUSTOMER);
        u = userRepository.save(u);

        // Sinh JWT token
        UserDetails ud = userDetailsService.loadUserByUsername(u.getEmail());
        String token = jwtService.generateToken(ud);

        AuthResponse resp = new AuthResponse();
        resp.setUser(userMapper.toResponse(u));
        resp.setToken(token);
        return resp;
    }
}
