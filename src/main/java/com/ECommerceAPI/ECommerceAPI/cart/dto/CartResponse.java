package com.ECommerceAPI.ECommerceAPI.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Long cartId;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
}
