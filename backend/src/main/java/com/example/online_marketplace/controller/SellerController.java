package com.example.online_marketplace.controller;

import com.example.online_marketplace.dto.SellerDto;
import com.example.online_marketplace.response.ApiResponse;
import com.example.online_marketplace.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sellers")
class SellerController {
    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }


    @GetMapping()
    public ApiResponse<Page<SellerDto>> findAllSellers(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return new ApiResponse<>(true, "All sellers retrieved successfully.",
                sellerService.findAllSellers(page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<SellerDto> findSellerById(@PathVariable Long id) {
        SellerDto seller = sellerService.findById(id);
        return new ApiResponse<>(true, "Seller retrieved successfully.", seller);
    }

}
