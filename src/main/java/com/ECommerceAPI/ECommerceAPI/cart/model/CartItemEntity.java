package com.ECommerceAPI.ECommerceAPI.cart.model;

import com.ECommerceAPI.ECommerceAPI.product.model.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cartItem")
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;
    @Column
    private Long quantity;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

}
