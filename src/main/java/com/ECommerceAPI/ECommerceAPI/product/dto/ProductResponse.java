package com.ECommerceAPI.ECommerceAPI.product.dto;

import com.ECommerceAPI.ECommerceAPI.product.model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long stockQuantity;
    private Category category;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;
}
