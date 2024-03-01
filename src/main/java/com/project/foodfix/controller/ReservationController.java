package com.project.foodfix.controller;

import com.project.foodfix.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create_reservation")
    public void createReservation(@RequestParam String userId, @RequestParam Long storeId, @RequestParam String userSelectedReservationDateTime) {
        // 프론트엔드에서 받아온 문자열 형태의 날짜를 LocalDateTime으로 변환
        LocalDateTime reservationDateTime = LocalDateTime.parse(userSelectedReservationDateTime, DateTimeFormatter.ISO_DATE_TIME);
        // 서비스 메서드 호출
        reservationService.createReservation(userId, storeId, reservationDateTime);
    }
}
