package com.example.online_marketplace.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SellerDto {
    private Long id;
    private String name;
    private List<ProductDto> products; // Seller'ın sattığı ürünler

}
