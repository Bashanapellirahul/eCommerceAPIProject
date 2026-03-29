package com.ECommerceAPI.ECommerceAPI.product.service;

import com.ECommerceAPI.ECommerceAPI.product.dto.ProductRequest;
import com.ECommerceAPI.ECommerceAPI.product.dto.ProductResponse;
import com.ECommerceAPI.ECommerceAPI.product.model.Category;
import com.ECommerceAPI.ECommerceAPI.product.model.ProductEntity;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductByName(String name);
    List<ProductResponse> getProductByCategory(Category category);
    List<ProductResponse> getProductByCategoryAndName(Category category, String  name);
//    List<ProductResponse> getActiveProductAndCategory(Category category);

    void deleteProduct(Long id);
    void restoreProduct(Long id);
}
