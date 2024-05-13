package com.project.foodfix.model.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationDTO {
    // 사용자측 정보
    private String user_id;
    private String user_phone;
    private String user_comments;
    private LocalDate reservation_date;
    private LocalTime reservation_time;
    private int people_cnt;

    // 매장측 정보
    private String store_phone;
    private String store_name;
    private Long store_id;

    // 예약 정보
    private Long reservation_id;
    private String reservation_status;

}