package com.project.foodfix.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long reservation_id; // 예약 번호

    private LocalDateTime reservation_time; // 예약 시간
    private Integer num_people; // 예약 인원
    private String user_comments; // 사용자 요구사항
    private String reservation_status; // 예약 상태 (예약 대기, 예약 완료, 예약 취소 등)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 예약한 사용자

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 예약한 매장

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Review review; // 해당 예약에 대한 리뷰 정보

}