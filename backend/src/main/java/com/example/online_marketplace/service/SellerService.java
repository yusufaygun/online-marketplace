package com.example.online_marketplace.service;

import com.example.online_marketplace.dto.SellerDto;
import com.example.online_marketplace.model.Product;
import com.example.online_marketplace.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;


public interface SellerService {

    SellerDto saveSeller(Seller seller);

    Page<SellerDto> findAllSellers(int page, int size);

    SellerDto findById(Long id);

    SellerDto updateSeller(Long id, Seller sellerDetails);

    void deleteSeller(Long id);

    SellerDto addProductToSeller(Long sellerId, Product product);

    SellerDto updateProduct(Long sellerId, Long productId, Product productDetails);

    SellerDto removeProductFromSeller(Long sellerId, Long productId);
}

