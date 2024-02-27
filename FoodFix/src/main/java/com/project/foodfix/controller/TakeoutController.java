package com.project.foodfix.controller;

import com.project.foodfix.model.dto.TakeoutDTO;
import com.project.foodfix.service.TakeoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping("/takeout")
public class TakeoutController {
    private final TakeoutService takeoutService;

    public TakeoutController(TakeoutService takeoutService) {
        this.takeoutService = takeoutService;
    }
    // 주문을 접수하는 API
    @PostMapping("/order")
    public ResponseEntity<String> placeTakeoutOrder(@RequestBody TakeoutDTO takeoutDTO) {
        try {
            // TakeoutService의 placeTakeoutOrder 메서드를 호출하여 주문을 처리합니다.
            takeoutService.placeTakeoutOrder(takeoutDTO);
            // 주문 성공시 200 OK 응답을 반환합니다.
            return ResponseEntity.ok("주문이 접수 되었습니다.");
        } catch (Exception e) {
            // 주문 실패시 400 Bad Request 응답을 반환합니다.
            return ResponseEntity.badRequest().body("주문이 실패했습니다. " + e.getMessage());
        }
    }
    // 주문 취소 API
    @PostMapping("/cancel/{takeoutOrderId}")
    public ResponseEntity<String> cancelTakeoutOrder(@PathVariable Long takeoutOrderId) {
        try {
            // TakeoutService의 cancelTakeoutOrder 메서드를 호출하여 주문 취소를 처리합니다.
            takeoutService.cancelTakeoutOrder(takeoutOrderId);
            // 주문 취소 성공시 200 OK 응답을 반환합니다.
            return ResponseEntity.ok("주문이 취소되었습니다.");
        } catch (Exception e) {
            // 주문 취소 실패시 400 Bad Request 응답을 반환합니다.
            return ResponseEntity.badRequest().body("주문 취소가 실패했습니다. " + e.getMessage());
        }
    }
}