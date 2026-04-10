package com.ECommerceAPI.ECommerceAPI.product.service.implementation;

import com.ECommerceAPI.ECommerceAPI.product.dto.ProductRequest;
import com.ECommerceAPI.ECommerceAPI.product.dto.ProductResponse;
import com.ECommerceAPI.ECommerceAPI.product.model.Category;
import com.ECommerceAPI.ECommerceAPI.product.model.ProductEntity;
import com.ECommerceAPI.ECommerceAPI.product.repository.ProductRepository;
import com.ECommerceAPI.ECommerceAPI.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        ProductEntity entity = new ProductEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setStockQuantity(request.getStockQuantity());
        entity.setCategory(request.getCategory());
        entity.setActive(true);
        entity.setImageUrl(request.getImageUrl());
        return mapToResponse(repository.save(entity));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return repository.findByActiveTrue()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductByName(String name) {
        return repository.findByActiveTrueAndNameContainingIgnoreCase(name)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductByCategory(Category category) {
        return repository.findByActiveTrueAndCategory(category)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Override
    public List<ProductResponse> getProductByCategoryAndName(Category category, String name) {
        return repository.findByActiveTrueAndCategoryAndNameContainingIgnoreCase(category, name)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        ProductEntity entity = repository.findById(id).orElseThrow(()->new RuntimeException("Product Not Found"));
        if(!entity.isActive()){
            throw new RuntimeException("Product already deleted");
        }
        entity.setActive(false);

        repository.save(entity);
    }

    @Override
    public void restoreProduct(Long id) {
        ProductEntity entity = repository.findById(id).orElseThrow(()->new RuntimeException("Product Not Found"));
        if(entity.isActive()){
            throw new RuntimeException("Product already restored");
        }
        entity.setActive(true);
        repository.save(entity);
    }

    @Override
    public Long getProductCount() {
        Long productCount = repository.count();
        System.out.println("Product Count is : "+ productCount);
        return productCount;
    }

    @Override
    public void deleteAllProducts(List<Long> ids) {
        List<ProductEntity> prods = repository.findAllById(ids);
        repository.deleteAllById(ids);
        repository.deleteAll(prods);
//        repository.deleteAllByIdInBatch();

//        if(prods.size() == ids.size()){
//
//        }
//
//        if(prods.equals(repository.findAllById(ids))){
//            repository.deleteAllById(ids);
//        }
//        else{
//
//        }

    }


    private ProductResponse mapToResponse(ProductEntity entity ){
        ProductResponse response = new ProductResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setStockQuantity(entity.getStockQuantity());
        response.setCategory(entity.getCategory());
        response.setActive(entity.isActive());
        response.setImageUrl(entity.getImageUrl());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;

    }
}
