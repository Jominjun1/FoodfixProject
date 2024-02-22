// Store.java
package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "store_id")
    private Long store_id;


    private String store_name;
    private String store_address;
    private String store_category;
    private String store_phone;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    @JoinColumn(name = "admin_id", nullable = false, updatable = false)
    private Admin admin;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private List<Menu> menus = new ArrayList<>();
}
