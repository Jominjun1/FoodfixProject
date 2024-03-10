package com.project.foodfix.model.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationDTO {
    private String user_id; // 사용자 ID
    private Long store_id; // 매장 ID
    private LocalDate reservation_date; // 예약 날짜
    private LocalTime reservation_time; // 예약 시간
    private int people_cnt; // 예약 인원
    private String user_comments; // 사용자 요구사항

}