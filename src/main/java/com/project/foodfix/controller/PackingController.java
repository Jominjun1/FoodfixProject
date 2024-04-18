package com.project.foodfix.controller;

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

    public PackingController(StoreService storeService) {
        this.storeService = storeService;
    }
    // 포장 주문 생성
    @PostMapping("/create")
    public ResponseEntity<String> createPacking(@RequestBody PackingDTO packingDTO){
        // 매장 포장 시도
        List<PackingDTO> packings = storeService.packingStore(packingDTO);

        if (packings != null) {
            return ResponseEntity.ok("포장 주문 성공");
        } else {
            return ResponseEntity.badRequest().body("포장 실패");
        }
    }
}
