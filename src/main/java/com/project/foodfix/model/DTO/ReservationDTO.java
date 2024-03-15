package com.project.foodfix.model.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationDTO {
    // 사용자측 정보
    private String user_id; // 사용자 ID
    private String user_phone; // 사용자 전화번호
    private String user_comments; // 사용자 요구사항
    private LocalDate reservation_date; // 예약 날짜
    private LocalTime reservation_time; // 예약 시간
    private int people_cnt; // 예약 인원

    // 매장측 정보
    private String store_phone; // 매장 전화번호
    private Long store_id; // 매장 ID
}