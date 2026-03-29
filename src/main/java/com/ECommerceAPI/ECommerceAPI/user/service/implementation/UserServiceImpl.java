package com.ECommerceAPI.ECommerceAPI.user.service.implementation;

import com.ECommerceAPI.ECommerceAPI.user.dto.AuthResponse;
import com.ECommerceAPI.ECommerceAPI.user.dto.LoginRequest;
import com.ECommerceAPI.ECommerceAPI.user.dto.RegisterRequest;
import com.ECommerceAPI.ECommerceAPI.user.model.Role;
import com.ECommerceAPI.ECommerceAPI.user.model.UserEntity;
import com.ECommerceAPI.ECommerceAPI.user.repository.UserRepository;
import com.ECommerceAPI.ECommerceAPI.security.JwtUtil;
import com.ECommerceAPI.ECommerceAPI.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse createUser(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        if(userRepository.existsByUserName(request.getUserName())){
            throw new RuntimeException("Username already exists");
        }
        UserEntity entity = new UserEntity();
        entity.setUserName(request.getUserName());
        entity.setEmail(request.getEmail());
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Password mismatch");
        }
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRole(Role.ROLE_USER);
        userRepository.save(entity);
        return AuthResponse.builder().token(null).message("User Registered Successfully").build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        UserEntity entity = userRepository.findByUserName(request.getUserName())
                .orElseThrow(()-> new RuntimeException("Username not found"));
        if(!passwordEncoder.matches(request.getPassword(), entity.getPassword()))
        {
            throw new RuntimeException("Incorrect password");
        }
        String token = jwtUtil.generateToken(entity.getUserName());
        return AuthResponse.builder().token(token).message("Login Successful").build();
    }


}
