package com.example.online_marketplace.mapper;

import com.example.online_marketplace.dto.SellerDto;
import com.example.online_marketplace.model.Seller;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@NoArgsConstructor
public class SellerMapper {

    public static SellerDto mapEntityToDto(Seller seller) {
        SellerDto sellerDto = new SellerDto();
        sellerDto.setId(seller.getId());
        sellerDto.setName(seller.getName());
        sellerDto.setProducts(seller.getProducts().stream()
                .map(ProductMapper::mapEntityToDto)
                .collect(Collectors.toList()));
        return sellerDto;
    }
}
