package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // 일련번호 사용
    private Long reservation_id; // 예약 주문번호
    private LocalDateTime reservation_Date; // 예약 주문 당시 시간
    private LocalDateTime reservationDateTime; // 예약날짜
    private String res_requirements; // 요구사항
    private Integer  res_Status; // 예약 주문 상태 (0: 접수중, 1: 예약완료 , 2 : 예약 취소 , 3 : 종료 )

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false, updatable = false) // (사용자) 일대다 (예약주문) 관계
    private User user; // 사용자 정보

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    @JoinColumn(name = "store_id", nullable = false, updatable = false) // (매장) 일대다 (예약주문) 관계
    private Store store; // 매장정보

}