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
    private String sellerName;  // Only the seller's name will be included in this DTO
    private Long sellerId; // And also the seller's ID

}
