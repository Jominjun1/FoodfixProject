package com.project.foodfix.model.DTO;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
public class StoreDTO {
    private Long store_id; // 매장 번호 + 유니크키 , 키값 자동 삽입

    private String store_name; // 매장 이름
    private String store_address; // 매장 주소
    private String store_category; // 매장 카테고리
    private String store_phone; // 매장 전화 번호
    private String res_status; // 예약 여부 ( 0 : 예약 불가능, 1 : 예약 가능)
    private String store_intro; // 매장 설명
    private String imagePath; // 이미지를 불러올 이미지 경로
    private Integer minimumTime; // 포장 최소 준비 시간 (분)
    private Integer res_max; // 예약 최대 가능팀 ( res_status 0이면 해당 값도 0 )
    private LocalTime openTime; // 오픈 시간
    private LocalTime closeTime; // 마감 시간
    private LocalTime reservationCancel; // 예약 취소 가능 시간

    public StoreDTO(Long store_id, String store_name, String store_address, String store_category, String store_phone, String res_status, String store_intro,
                    String imagePath, Integer minimumTime, Integer res_max, LocalTime openTime, LocalTime closeTime, LocalTime reservationCancel) {
        this.store_id = store_id;
        this.store_name = store_name;
        this.store_address = store_address;
        this.store_category = store_category;
        this.store_phone = store_phone;
        this.res_status = res_status;
        this.store_intro = store_intro;
        this.imagePath = imagePath;
        this.minimumTime = minimumTime;
        this.res_max = res_max;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.reservationCancel = reservationCancel;
    }
}
