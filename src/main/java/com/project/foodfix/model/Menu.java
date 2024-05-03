package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // 일련번호 사용
    @Column(name = "menu_id")
    private Long menu_id; // 메뉴 번호 + 기본키

    private String menu_name; // 메뉴 이름
    private String explanation; //메뉴 설명
    private Double menu_price; // 메뉴 가격

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "photo_id")
    @JsonBackReference
    private Photo menuPhoto;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false, updatable = false) // (매장) 일대다 (메뉴) 관계
    @JsonIgnore
    private Store store; // 매장 정보


}
