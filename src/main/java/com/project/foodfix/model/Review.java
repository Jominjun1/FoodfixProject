package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long review_id; // 리뷰 번호
    private String comment; // 리뷰 내용
    private Integer rating; // 평점
    private LocalDate reviewDate; // 리뷰 작성 일자

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation; // 리뷰가 속한 예약

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 리뷰 작성한 사용자

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 리뷰 대상 매장


}