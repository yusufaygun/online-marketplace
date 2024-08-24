package com.example.online_marketplace.service;

import com.example.online_marketplace.dto.ProductDto;
import com.example.online_marketplace.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductDto saveProduct(Product product);

    Page<ProductDto> findAllProducts(int page, int size);

    ProductDto findById(Long id);

    void deleteProduct(Long id);
}
