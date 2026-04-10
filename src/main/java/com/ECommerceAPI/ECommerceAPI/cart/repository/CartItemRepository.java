package com.ECommerceAPI.ECommerceAPI.cart.repository;

import com.ECommerceAPI.ECommerceAPI.cart.model.CartEntity;
import com.ECommerceAPI.ECommerceAPI.cart.model.CartItemEntity;
import com.ECommerceAPI.ECommerceAPI.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByCart(CartEntity cart);
    Optional<CartItemEntity> findByCartAndProduct(CartEntity cart, ProductEntity product);

    Optional<CartItemEntity> findByIdAndCart(Long id, CartEntity cartItem);

}
