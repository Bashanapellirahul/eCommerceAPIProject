package com.ECommerceAPI.ECommerceAPI.product.controller;

import com.ECommerceAPI.ECommerceAPI.product.dto.ProductRequest;
import com.ECommerceAPI.ECommerceAPI.product.dto.ProductResponse;
import com.ECommerceAPI.ECommerceAPI.product.model.Category;
import com.ECommerceAPI.ECommerceAPI.product.service.implementation.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request){
        return ResponseEntity.ok(service.createProduct(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProductByName(@RequestParam String name){
        return  ResponseEntity.ok(service.getProductByName(name));
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductResponse>> getByCategory(@RequestParam Category category){
        return ResponseEntity.ok(service.getProductByCategory(category));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> getByCategoryAndName(
            @RequestParam Category category, @RequestParam String name){
        return ResponseEntity.ok(service.getProductByCategoryAndName(category,name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
        return ResponseEntity.ok("Product Deleted successfully");

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreProduct(@PathVariable Long id){
        service.restoreProduct(id);
        return ResponseEntity.ok("Product restored successfully");
    }


}
