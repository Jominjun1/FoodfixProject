package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "store")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "store_id")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "store_id")
    private Long store_id; // 매장 번호 + 유니크키 , 키값 자동 삽입

    private String store_name; // 매장 이름
    private String store_address; // 매장 주소
    private String store_category; // 매장 카테고리
    private String store_phone; // 매장 전화 번호
    private String res_status; // 예약 여부 ( 0 : 예약 불가능, 1 : 예약 가능)
    private String store_intro; // 매장 설명
    private Integer minimumTime; // 포장 최소 준비 시간 (분)
    private Integer res_max; // 예약 최대 가능팀 ( res_status 0이면 해당 값도 0 )
    private LocalTime openTime; // 오픈 시간
    private LocalTime closeTime; // 마감 시간
    private LocalTime reservationCancel; // 예약 취소 가능 시간

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "admin_id", nullable = false, unique = true)
    private Admin admin;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL , fetch = FetchType.EAGER) // (매장) 일대다 (메뉴) 관계
    private List<Menu> menus = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Packing> packings = new ArrayList<>();
}