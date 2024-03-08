package com.project.foodfix.controller;

import com.project.foodfix.model.DTO.PackableStoreDTO;
import com.project.foodfix.model.DTO.ReservableStoreDTO;
import com.project.foodfix.service.Store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // 포장 검색
    @GetMapping("/packable")
    public ResponseEntity<List<PackableStoreDTO>> searchPackableStores(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String menuName) {
        // 포장 가능한 매장 검색 결과 반환
        List<PackableStoreDTO> result = storeService.searchPackableStores(category, storeName, menuName);
        return ResponseEntity.ok(result);
    }

    // 예약 검색
    @GetMapping("/reservable")
    public ResponseEntity<List<ReservableStoreDTO>> searchReservableStores(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String storeName) {
        // 예약 가능한 매장 검색 결과 반환
        List<ReservableStoreDTO> result = storeService.searchReservableStores(category, storeName);
        return ResponseEntity.ok(result);
    }
}