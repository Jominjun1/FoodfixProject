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
    private Long store_id; // 매장 번호 + 유니크키 , 키값 자동 삽입

    private String store_name; // 매장 이름
    private String store_address; // 매장 주소
    private String store_category; // 매장 카테고리
    private String store_phone; // 매장 전화 번호

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    @JoinColumn(name = "admin_id", nullable = false, updatable = false)
    private Admin admin;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL , fetch = FetchType.EAGER) // (매장)일대다(메뉴) 관계
    private List<Menu> menus = new ArrayList<>();
}
