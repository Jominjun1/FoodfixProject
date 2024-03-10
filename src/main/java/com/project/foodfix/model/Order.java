package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    private Long order_id; // 주문 번호
    private String payment_method; // 결제 수단
    private String user_comments; // 사용자 요구사항
    private String order_status; // 주문 상태
    private LocalDateTime order_time; // 주문 시간
    private LocalDateTime pickup_time; // 픽업 시간

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 주문한 사용자

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 주문한 매장

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); // 주문한 항목 목록

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Review review; // 해당 주문에 대한 리뷰 정보
}