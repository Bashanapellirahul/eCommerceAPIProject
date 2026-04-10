package com.ECommerceAPI.ECommerceAPI.product.controller;

import com.ECommerceAPI.ECommerceAPI.product.dto.ProductRequest;
import com.ECommerceAPI.ECommerceAPI.product.dto.ProductResponse;
import com.ECommerceAPI.ECommerceAPI.product.model.Category;
import com.ECommerceAPI.ECommerceAPI.product.service.implementation.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
//    @Autowired
    private final ProductServiceImpl service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request){
//        return ResponseEntity.ok(service.createProduct(request));
        return new ResponseEntity<>(service.createProduct(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
//        return ResponseEntity.ok(service.getAllProducts());
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProductByName(@RequestParam String name){
//        return  ResponseEntity.ok(service.getProductByName(name));
        return new ResponseEntity<>(service.getProductByName(name), HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductResponse>> getByCategory(@RequestParam Category category){
//        return ResponseEntity.ok(service.getProductByCategory(category));
        return new ResponseEntity<>(service.getProductByCategory(category), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> getByCategoryAndName(
            @RequestParam Category category, @RequestParam String name){
//        return ResponseEntity.ok(service.getProductByCategoryAndName(category,name));
        return new ResponseEntity<>(service.getProductByCategoryAndName(category,name), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
        return ResponseEntity.ok("Product Deleted successfully");
//        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreProduct(@PathVariable Long id){
        service.restoreProduct(id);
        return ResponseEntity.ok("Product restored successfully");
//        return new ResponseEntity<>(HttpStatus.ACCEPTED,"Product");
    }

    @PreAuthorize(("hasRole('ADMIN')"))
    @GetMapping("/count")
    public ResponseEntity<Long> getProductCount(){
//        service.getProductCount();
        return new ResponseEntity<>(service.getProductCount(), HttpStatus.OK);
    }



}
