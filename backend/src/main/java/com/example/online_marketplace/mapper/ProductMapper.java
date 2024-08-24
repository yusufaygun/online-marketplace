package com.example.online_marketplace.mapper;

import com.example.online_marketplace.dto.ProductDto;
import com.example.online_marketplace.model.Product;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductMapper {

    public static ProductDto mapEntityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setSellerName(product.getSeller() != null ? product.getSeller().getName() : null);
        productDto.setSellerId(product.getSeller() != null ? product.getSeller().getId() : null);
        return productDto;
    }
}
