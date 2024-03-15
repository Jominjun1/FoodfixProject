package com.project.foodfix.controller;


import com.project.foodfix.model.DTO.ReservationDTO;
import com.project.foodfix.service.Store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reservation")
@Controller
public class ReservationController {

    private final StoreService storeService;

    @Autowired
    public ReservationController(StoreService storeService) {
        this.storeService = storeService;
    }

    // 예약 주문 생성
    @PostMapping("/create")
    public ResponseEntity<String> createReservation(@RequestBody ReservationDTO reservationDTO) {
        // 매장 예약 시도
        List<ReservationDTO> reservationResult = storeService.reservationStore(reservationDTO);

        // 매장 예약 결과에 따라 응답 처리
        if (reservationResult != null) {
            return ResponseEntity.ok("매장 예약 주문 성공");
        } else {
            return ResponseEntity.badRequest().body("매장 예약 실패");
        }
    }
}
