package com.example.online_marketplace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private double price;
    private String description;
    private String sellerName;  // Seller'ın sadece adı bu DTO'ya dahil olacak
    private Long sellerId; // şaka ve de idsi

}
