package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderItem_id; // 주문 항목 번호
    private Integer quantity; // 수량
    private Double item_price; // 항목 가격
    private String item_comments; // 항목 요구사항

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu; // 주문된 메뉴

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // 속한 주문
}