package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "takeout_order")
public class TakeoutOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long takeout_id; // 주문 번호

    private LocalDateTime orderDateTime; // 주문 시간
    private LocalDateTime estimatedCompletionTime; // 포장 예상 완료 시간
    private String requirements; // 사용자 요구사항
    private Integer  timeStatus; // 포장 주문 시간 결정 ( 0 : 매장의 최소 주문 시간 , 1 : 사용자가 직접 선택 )
    private Integer  timeOrder; // 사용자가 선택한 포장 주문 시간 ( TimeStatus가 1일때만 사용)
    private Integer  orderStatus; // 주문 상태 (0: 접수중, 1: 준비중, 2: 포장완료, 3: 접수 취소 , 4: 주문 완료)
    private Integer  paymentStatus; // 결제 상태 (0: 미결제, 1: 결제 완료)
    private Integer  paymentMethod; // 결제 방법 (0: 앱결제, 1: 매장결제)
    private double totalMenuPrice; // 총 메뉴 가격

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

    // OrderItem 엔터티와의 일대다 관계 설정
    @OneToMany(mappedBy = "takeoutOrder", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

}