package com.ECommerceAPI.ECommerceAPI.product.repository;

import com.ECommerceAPI.ECommerceAPI.product.model.Category;
import com.ECommerceAPI.ECommerceAPI.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByActiveTrueAndNameContainingIgnoreCase(String name);
//    List<ProductEntity> findByActiveTrueAndfindByCategory(Category category);
    List<ProductEntity> findByActiveTrueAndCategoryAndNameContainingIgnoreCase(Category category, String name);
    List<ProductEntity>  findByActiveTrue();
    List<ProductEntity> findByActiveTrueAndCategory(Category category);


}
