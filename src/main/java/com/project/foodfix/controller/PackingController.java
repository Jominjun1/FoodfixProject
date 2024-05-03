package com.project.foodfix.controller;

import com.project.foodfix.config.WebSocketHandler;
import com.project.foodfix.model.DTO.PackingDTO;
import com.project.foodfix.service.Store.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/packing")
@Controller
public class PackingController {
    private final StoreService storeService;
    private final WebSocketHandler webSocketHandler;

    public PackingController(StoreService storeService, WebSocketHandler webSocketHandler) {
        this.storeService = storeService;
        this.webSocketHandler = webSocketHandler;
    }
    // 포장 주문 생성
    @PostMapping("/create")
    public ResponseEntity<String> createPacking(@RequestBody PackingDTO packingDTO)  {
        // 매장 포장 시도
        List<PackingDTO> packings = storeService.packingStore(packingDTO);
        if (packings != null) {
            webSocketHandler.sendPackingOrder(packingDTO.getStore_id(), packingDTO.getMinimumTime());
            return ResponseEntity.ok("포장 주문 성공");
        } else {
            return ResponseEntity.badRequest().body("포장 실패");
        }
    }
}
