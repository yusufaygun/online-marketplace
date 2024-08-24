package com.example.online_marketplace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_products")
public class Product extends BaseEntity implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "description")
    private String description;

    // this may be unnecessary, think later in case of performance etc.
    // to keep users that added this product to their fav list
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favoriteProducts")
    private Set<User> favoritedByUsers = new HashSet<>();

    // to keep the seller of this product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", referencedColumnName = "id", nullable = true)
    private Seller seller;




}
