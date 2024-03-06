package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // 일련번호 사용
    private Long orderId;

    private String orderStatus;     // 주문 상태 0: 주문접수중, 1: 주문진행 중, 2: 주문완료됨, 3: 주문취소됨
    private LocalDateTime orderTime;  // 주문 시간

    // 주문한 사용자와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    // 주문된 가게와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

    // 주문된 메뉴와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private Menu menu;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();
}