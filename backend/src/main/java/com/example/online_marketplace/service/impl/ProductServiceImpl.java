package com.example.online_marketplace.service.impl;

import com.example.online_marketplace.dto.ProductDto;
import com.example.online_marketplace.exception.EntityNotFoundException;
import com.example.online_marketplace.mapper.ProductMapper;
import com.example.online_marketplace.model.Product;
import com.example.online_marketplace.repository.ProductRepository;
import com.example.online_marketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapEntityToDto(savedProduct);
    }

    @Override
    public Page<ProductDto> findAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);

        // Page<ProductDto>'ya dönüştür
        List<ProductDto> productDtos = productPage.stream()
                .map(ProductMapper::mapEntityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with the given ID."));
        return ProductMapper.mapEntityToDto(product);
    }



    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with the given ID."));
        productRepository.delete(product);
    }
}
