package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long reservation_id; // 예약 번호

    private LocalDate reservation_date; // 예약 날짜
    private LocalTime reservation_time; // 예약 시간
    private Integer num_people; // 예약 인원
    private String user_comments; // 사용자 요구사항
    @Column(columnDefinition = "VARCHAR(1) DEFAULT '0'")
    private String reservation_status; // 예약 상태 (0 : 예약 대기, 1 : 예약 성공, 2 : 예약 취소 3 : 예약 완료)

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 예약한 사용자

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 예약한 매장
}