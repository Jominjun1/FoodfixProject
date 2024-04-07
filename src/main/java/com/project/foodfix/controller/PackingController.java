package com.project.foodfix.controller;

import com.project.foodfix.UserType;
import com.project.foodfix.model.Admin;
import com.project.foodfix.model.DTO.PackingDTO;
import com.project.foodfix.service.Store.StoreService;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/create")
    public ResponseEntity<String> createPacking(@RequestBody PackingDTO packingDTO){
        // 매장 포장 시도
        List<PackingDTO> packings = storeService.packingStore(packingDTO);
        // 매장 포장 결과에 따라 응답 처리
        if (packings != null) {
            return ResponseEntity.ok("포장 주문 성공");
        } else {
            return ResponseEntity.badRequest().body("포장 실패");
        }
    }
    // 포장 주문 승락/거절
    @PutMapping("/orderStatus")
    public ResponseEntity<Object> updatePackingStatus(){
        return ResponseEntity.badRequest().body("실패");
    }
}
