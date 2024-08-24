package com.example.online_marketplace.service.impl;

import com.example.online_marketplace.dto.SellerDto;
import com.example.online_marketplace.exception.EntityNotFoundException;
import com.example.online_marketplace.mapper.SellerMapper;
import com.example.online_marketplace.model.Product;
import com.example.online_marketplace.model.Seller;
import com.example.online_marketplace.repository.SellerRepository;
import com.example.online_marketplace.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public SellerDto saveSeller(Seller seller) {
        Seller savedSeller = sellerRepository.save(seller);
        return SellerMapper.mapEntityToDto(savedSeller);
    }

    @Override
    public Page<SellerDto> findAllSellers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Seller> sellerPage = sellerRepository.findAll(pageable);

        // Page<SellerDto>'ya dönüştürme
        List<SellerDto> sellerDtos = sellerPage.stream()
                .map(SellerMapper::mapEntityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(sellerDtos, pageable, sellerPage.getTotalElements());
    }

    @Override
    public SellerDto findById(Long id) {
        Seller seller = getSellerById(id);
        return SellerMapper.mapEntityToDto(seller);
    }

    @Override
    public SellerDto updateSeller(Long id, Seller sellerDetails) {
        Seller existingSeller = getSellerById(id);
        existingSeller.setName(sellerDetails.getName());

        // Güncellenmiş seller'ı veritabanına kaydet
        Seller savedSeller = sellerRepository.save(existingSeller);
        return SellerMapper.mapEntityToDto(savedSeller);
    }


    @Override
    public void deleteSeller(Long id) {
        Seller seller = getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public SellerDto addProductToSeller(Long sellerId, Product product) {
        Seller seller = getSellerById(sellerId);

        product.setSeller(seller);
        seller.getProducts().add(product);

        sellerRepository.save(seller);
        return SellerMapper.mapEntityToDto(seller);
    }

    @Override
    public SellerDto updateProduct(Long sellerId, Long productId, Product productDetails) {
        Seller seller = getSellerById(sellerId);

        Product existingProduct = seller.getProducts().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product not found with the given ID."));

        // Mevcut product'ı gelen bilgilerle güncelle
        existingProduct.setName(productDetails.getName());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setDescription(productDetails.getDescription());

        sellerRepository.save(seller);
        return SellerMapper.mapEntityToDto(seller);
    }


    @Override
    public SellerDto removeProductFromSeller(Long sellerId, Long productId) {
        Seller seller = getSellerById(sellerId);

        Product productToRemove = seller.getProducts().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product not found with the given ID."));

        seller.getProducts().remove(productToRemove);
        productToRemove.setSeller(null);

        sellerRepository.save(seller);
        return SellerMapper.mapEntityToDto(seller);
    }

    private Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found with the given ID."));
    }
}
