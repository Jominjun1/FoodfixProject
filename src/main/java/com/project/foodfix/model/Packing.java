package com.project.foodfix.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "packing")
public class Packing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long packing_id; // 포장 번호

    private LocalDate packing_date; // 포장일
    private LocalTime packing_time; // 포장시간
    private String user_comments; // 사용자 요구사항
    @Column(columnDefinition = "VARCHAR(1) DEFAULT '0'")
    private String packing_status; // 포장 상태 (0 : 주문중 ,  1 : 주문 접수, 2 : 주문 취소 3 : 주문 완료)
    @Column(columnDefinition = "VARCHAR(1) DEFAULT '0'")
    private String payment_type; // 결재 방법 ( 0 : 앱결재 , 1 : 방문 결재)

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 포장한 사용자

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 포장한 매장

    @OneToMany(mappedBy = "packing")
    private List<MenuItem> menuitem = new ArrayList<>();

    @Transient
    private String store_name;

}
