package com.ECommerceAPI.ECommerceAPI.user.service;

import com.ECommerceAPI.ECommerceAPI.user.dto.AuthResponse;
import com.ECommerceAPI.ECommerceAPI.user.dto.LoginRequest;
import com.ECommerceAPI.ECommerceAPI.user.dto.RegisterRequest;

public interface UserService {
    AuthResponse createUser(RegisterRequest request);
    AuthResponse login(LoginRequest request);
//    AuthResponse findUserById(Long id);
}
