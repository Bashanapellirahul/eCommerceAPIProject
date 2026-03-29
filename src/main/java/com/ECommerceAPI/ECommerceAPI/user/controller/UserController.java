package com.ECommerceAPI.ECommerceAPI.user.controller;

import com.ECommerceAPI.ECommerceAPI.user.dto.AuthResponse;
import com.ECommerceAPI.ECommerceAPI.user.dto.LoginRequest;
import com.ECommerceAPI.ECommerceAPI.user.dto.RegisterRequest;
import com.ECommerceAPI.ECommerceAPI.user.service.implementation.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody RegisterRequest request){
        AuthResponse response = service.createUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userLogin(@Valid @RequestBody LoginRequest request){
        AuthResponse response = service.login(request);
        return ResponseEntity.ok(response);
    }
}
