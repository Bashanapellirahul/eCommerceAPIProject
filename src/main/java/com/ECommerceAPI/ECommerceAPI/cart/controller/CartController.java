package com.ECommerceAPI.ECommerceAPI.cart.controller;

import com.ECommerceAPI.ECommerceAPI.cart.dto.CartItemRequest;
import com.ECommerceAPI.ECommerceAPI.cart.dto.CartResponse;
import com.ECommerceAPI.ECommerceAPI.cart.service.implementation.CartServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartServiceImpl cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getMyCart(){
        return ResponseEntity.ok(cartService.getMyCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody CartItemRequest cartItemRequest){
        return ResponseEntity.ok(cartService.addToCart(cartItemRequest));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateCartItem(@PathVariable Long itemId,@RequestBody CartItemRequest cartItemRequest){
        return ResponseEntity.ok(cartService.updateCartItem(itemId,cartItemRequest));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeCartItem( @PathVariable Long itemId){
        return ResponseEntity.ok(cartService.removeCartItem(itemId));
    }


}
