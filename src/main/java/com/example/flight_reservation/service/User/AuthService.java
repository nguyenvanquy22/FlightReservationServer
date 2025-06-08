package com.example.flight_reservation.service.User;

import com.example.flight_reservation.dto.request.AuthRequest;
import com.example.flight_reservation.dto.request.UserRequest;
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
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        // 2. Load lại thông tin user
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with username: " + req.getUsername()));

        // 3. Sinh JWT token
        UserDetails ud = userDetailsService.loadUserByUsername(u.getUsername());
        String token = jwtService.generateToken(ud);

        AuthResponse resp = new AuthResponse();
        resp.setUser(userMapper.toResponse(u));
        resp.setToken(token);
        return resp;
    }

    // Đăng ký
    public AuthResponse register(UserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User u = userMapper.toEntity(req);
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(req.getRole() != null ? req.getRole() : UserRole.CUSTOMER);
        u = userRepository.save(u);

        // Sinh JWT token
        UserDetails ud = userDetailsService.loadUserByUsername(u.getUsername());
        String token = jwtService.generateToken(ud);

        AuthResponse resp = new AuthResponse();
        resp.setUser(userMapper.toResponse(u));
        resp.setToken(token);
        return resp;
    }
}
