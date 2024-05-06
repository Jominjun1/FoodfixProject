package com.project.foodfix.controller;

import com.project.foodfix.config.WebSocketHandler;
import com.project.foodfix.model.DTO.PackingDTO;
import com.project.foodfix.model.DTO.ReservationDTO;
import com.project.foodfix.service.Store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order")
@Controller
public class OrderController {

    private final StoreService storeService;
    private final WebSocketHandler webSocketHandler;

    @Autowired
    public OrderController(StoreService storeService, WebSocketHandler webSocketHandler) {
        this.storeService = storeService;
        this.webSocketHandler = webSocketHandler;
    }

    // 예약 주문 생성
    @PostMapping("/reservation")
    public ResponseEntity<String> createReservation(@RequestBody ReservationDTO reservationDTO) {
        List<ReservationDTO> reservationResult = storeService.reservationStore(reservationDTO);

        if (reservationResult != null) {
            webSocketHandler.sendPacking(reservationDTO.getStore_id(), "예약 주문 생성");
            return ResponseEntity.ok("예약 주문 성공");
        } else {
            return ResponseEntity.badRequest().body("예약 실패");
        }
    }
    // 포장 주문 생성
    @PostMapping("/packing")
    public ResponseEntity<String> createPacking(@RequestBody PackingDTO packingDTO) {
        List<PackingDTO> packings = storeService.packingStore(packingDTO);
        if (packings != null) {
            webSocketHandler.sendPacking(packingDTO.getStore_id(), "포장 주문 생성");
            return ResponseEntity.ok("포장 주문 성공");
        } else {
            return ResponseEntity.badRequest().body("포장 실패");
        }
    }
}

