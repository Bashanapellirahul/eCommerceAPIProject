package com.ECommerceAPI.ECommerceAPI.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse<T> {
    private String token;
    private String message;
}
