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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_users")
public class User extends BaseEntity implements Serializable {

    @Column(name = "username", unique = true, nullable = false, columnDefinition = "VARCHAR(20)")
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "active", nullable = false)
    private boolean active; // New field to indicate if the account is active

    // to keep users role
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // a table to keep user fav list
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_favorite_list",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> favoriteProducts = new HashSet<>();

    // a table to keep user black list (blocked sellers)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_black_list",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "seller_id")
    )
    private Set<Seller> blacklistedSellers = new HashSet<>();


}
