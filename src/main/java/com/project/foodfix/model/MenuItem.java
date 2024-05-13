package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "menu_item")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long menuitem_id;

    private String menu_name;
    private double menu_price;
    private Double totalPrice; // 총 가격
    private int quantity; // 메뉴 수량

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "packing_id", nullable = false)
    private Packing packing; // 매장 정보

}
