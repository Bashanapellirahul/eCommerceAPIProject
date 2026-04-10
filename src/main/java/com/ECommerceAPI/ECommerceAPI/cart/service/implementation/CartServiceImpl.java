package com.ECommerceAPI.ECommerceAPI.cart.service.implementation;

import com.ECommerceAPI.ECommerceAPI.cart.dto.CartItemRequest;
import com.ECommerceAPI.ECommerceAPI.cart.dto.CartItemResponse;
import com.ECommerceAPI.ECommerceAPI.cart.dto.CartResponse;
import com.ECommerceAPI.ECommerceAPI.cart.model.CartEntity;
import com.ECommerceAPI.ECommerceAPI.cart.model.CartItemEntity;
import com.ECommerceAPI.ECommerceAPI.cart.repository.CartItemRepository;
import com.ECommerceAPI.ECommerceAPI.cart.repository.CartRepository;
import com.ECommerceAPI.ECommerceAPI.product.model.ProductEntity;
import com.ECommerceAPI.ECommerceAPI.product.repository.ProductRepository;
import com.ECommerceAPI.ECommerceAPI.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl{
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    private UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private CartEntity getOrCreateCart(UserEntity userEntity){
        return cartRepository.findByUser(userEntity)
                .orElseGet(()->{
                    CartEntity cartEntity = new CartEntity();
                    cartEntity.setUser(userEntity);
                    return cartRepository.save(cartEntity);
                });
    }

    public CartResponse addToCart(CartItemRequest request){
        if(request.getQuantity()<=0){
            throw new RuntimeException("Product quantity should be at least one");
        }

        UserEntity userEntity = getCurrentUser();
        CartEntity cartEntity = getOrCreateCart(userEntity);
        ProductEntity productEntity = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new RuntimeException("Product not found"));
        if(!productEntity.isActive()){
            throw new RuntimeException("Product is not active");
        }

        if (request.getQuantity() > productEntity.getStockQuantity()){
            throw new RuntimeException("Requested quantity exceeds available stock");
        }

        Optional<CartItemEntity> existingItem = cartItemRepository.findByCartAndProduct(cartEntity, productEntity);
        if(existingItem.isPresent()){
            CartItemEntity cartItemEntity = existingItem.get();
            Long newQuantity = cartItemEntity.getQuantity()+ request.getQuantity();
            if(newQuantity> productEntity.getStockQuantity()){
                throw new RuntimeException("Total quantity exceeds available stocks");
            }

            cartItemEntity.setQuantity(newQuantity);
            cartItemRepository.save(cartItemEntity);
        }
        else {
            CartItemEntity newItem = new CartItemEntity();
            newItem.setCart(cartEntity);
            newItem.setProduct(productEntity);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }
        return buildCartResponse(cartEntity);
    }

    private CartResponse buildCartResponse(CartEntity cartEntity) {

        List<CartItemEntity> items = cartItemRepository.findByCart(cartEntity);

        List<CartItemResponse> itemResponses = items.stream()
                .map(this::mapToCartItemResponse)
                .toList();

        BigDecimal total = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartResponse response = new CartResponse();
        response.setCartId(cartEntity.getId());
        response.setItems(itemResponses);
        response.setTotalAmount(total);

        return response;
    }

    private CartItemResponse mapToCartItemResponse(CartItemEntity item) {

        CartItemResponse response = new CartItemResponse();
        response.setCartItemId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        BigDecimal price = item.getProduct().getPrice();
        response.setPrice(price);
        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
        response.setSubtotal(subtotal);
        return response;
    }

    public CartResponse getMyCart(){
        UserEntity userEntity = getCurrentUser();
        Optional<CartEntity> optionalCart = cartRepository.findByUser(userEntity);
        if(optionalCart.isEmpty()){
            CartResponse emptyCart = new CartResponse();
            emptyCart.setCartId(null);
            emptyCart.setItems(List.of());
            emptyCart.setTotalAmount(BigDecimal.ZERO);
            return emptyCart;
        }

        return buildCartResponse(optionalCart.get());
    }

    public CartResponse updateCartItem(Long itemId, CartItemRequest cartItemRequest){
        if(cartItemRequest.getQuantity()<0){
            throw new RuntimeException("Quantity cannot be negative");
        }

        UserEntity userEntity = getCurrentUser();
        CartEntity cartEntity = getOrCreateCart(userEntity);

        CartItemEntity cartItemEntity = cartItemRepository.findByIdAndCart(itemId,cartEntity)
                .orElseThrow(()-> new RuntimeException("Cart item not found"));
        if(cartItemRequest.getQuantity()==0){
            cartItemRepository.delete(cartItemEntity);
            return buildCartResponse(cartEntity);
        }

        ProductEntity productEntity = cartItemEntity.getProduct();
        if(cartItemRequest.getQuantity()>productEntity.getStockQuantity()){
            throw new RuntimeException("Requested quantity exceeds available stock");
        }

        cartItemEntity.setQuantity(cartItemRequest.getQuantity());
        cartItemRepository.save(cartItemEntity);
        return buildCartResponse(cartEntity);
    }

    public  CartResponse removeCartItem(Long itemId){
        UserEntity userEntity = getCurrentUser();
        CartEntity cartEntity = cartRepository.findByUser(userEntity)
                .orElseThrow(()-> new RuntimeException("Cart not found") );
        CartItemEntity cartItemEntity = cartItemRepository.findByIdAndCart(itemId, cartEntity)
                .orElseThrow(()-> new RuntimeException("Cart Item not found"));
        cartItemRepository.delete(cartItemEntity);
        return buildCartResponse(cartEntity);
    }
}
