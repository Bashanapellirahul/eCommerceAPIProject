package com.ECommerceAPI.ECommerceAPI.product.dto;

import com.ECommerceAPI.ECommerceAPI.product.model.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest
{
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @Positive
    private BigDecimal price;
    private Category category;
    @NotNull
    @PositiveOrZero
    private Long stockQuantity;
    private String imageUrl;
}
