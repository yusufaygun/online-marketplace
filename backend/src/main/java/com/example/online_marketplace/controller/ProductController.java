package com.example.online_marketplace.controller;

import com.example.online_marketplace.dto.ProductDto;
import com.example.online_marketplace.response.ApiResponse;
import com.example.online_marketplace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RestController
@RequestMapping("/products")
class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ApiResponse<Page<ProductDto>> findAllProducts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return new ApiResponse<>(true, "All products retrieved successfully.",
                productService.findAllProducts(page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDto> findProductById(@PathVariable Long id) {
        ProductDto product = productService.findById(id);
        return new ApiResponse<>(true, "Product retrieved successfully.", product);
    }

}
