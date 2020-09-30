package com.abhilash.ecommerce.orderservice.service;

import com.abhilash.ecommerce.orderservice.dto.ProductDto;
import com.abhilash.ecommerce.orderservice.exception.BadRequestException;
import com.abhilash.ecommerce.orderservice.model.Product;
import com.abhilash.ecommerce.orderservice.repository.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ProductService {
    private ProductRepo productRepo;

    @Synchronized
    public ProductDto createProduct(ProductDto productDto) {
        return mapToDto(productRepo.save(mapToEntity(productDto)));
    }

    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().parallelStream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProduct(String productId) {
        Product product = productRepo.findById(UUID.fromString(productId)).orElseThrow(() -> new BadRequestException("Invalid product Id - " + productId));
        return mapToDto(product);
    }

    @Synchronized
    @Transactional
    public ProductDto updateProduct(String productId, ProductDto productDto) {
        if (!productRepo.existsById(UUID.fromString(productId))) {
            throw new BadRequestException("Invalid product Id - " + productId);
        }
        Product product = mapToEntity(productDto);
        product.setId(UUID.fromString(productId));
        return mapToDto(productRepo.save(product));
    }

    @Synchronized
    @Transactional
    public void removeProduct(String productId) {
        if (!productRepo.existsById(UUID.fromString(productId))) {
            throw new BadRequestException("Invalid product Id - " + productId);
        }
        productRepo.deleteById(UUID.fromString(productId));
    }

    private Product mapToEntity(ProductDto productDto) {
        return Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .build();
    }

    private ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}