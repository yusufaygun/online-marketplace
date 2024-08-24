package com.example.online_marketplace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// bu sellers ayrı bir role olabilir, onu düşün
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_sellers")
public class Seller extends BaseEntity implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    // to keep the products of this seller
    @OneToMany (fetch = FetchType.EAGER, mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

}
