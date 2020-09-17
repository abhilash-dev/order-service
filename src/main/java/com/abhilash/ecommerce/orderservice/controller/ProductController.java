package com.abhilash.ecommerce.orderservice.controller;

import com.abhilash.ecommerce.orderservice.dto.ProductDto;
import com.abhilash.ecommerce.orderservice.service.ProductService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class exposes resources to interact with product entity
 */
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/product")
@Api(description = "Resources related to Product entity")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(value = "/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") String productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PutMapping(value = "/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") String productId, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(productId, productDto));
    }

    @DeleteMapping(value = "/{productId}")
    public ResponseEntity removeProduct(@PathVariable("productId") String productId) {
        productService.removeProduct(productId);
        return ResponseEntity.ok().build();
    }
}
